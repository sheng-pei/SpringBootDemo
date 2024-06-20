/*
 * Copyright 2014-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.example.springsession;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.*;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

/**
 * Switches the {@link HttpSession} implementation to be backed by a
 * {@link Session}.
 *
 * The {@link SessionRepositoryFilter} wraps the
 * {@link HttpServletRequest} and overrides the methods to get an
 * {@link HttpSession} to be backed by a
 * {@link Session} returned by the
 * {@link SessionRepository}.
 *
 * The {@link SessionRepositoryFilter} uses a {@link HttpSessionIdResolver} (default
 * {@link CookieHttpSessionIdResolver}) to bridge logic between an
 * {@link HttpSession} and the
 * {@link Session} abstraction. Specifically:
 *
 * <ul>
 * <li>The session id is looked up using
 * {@link HttpSessionIdResolver#resolveSessionIds(HttpServletRequest)}
 * . The default is to look in a cookie named SESSION.</li>
 * <li>The session id of newly created {@link Session} is sent
 * to the client using
 * {@link HttpSessionIdResolver#setSessionId(HttpServletRequest, HttpServletResponse, String)}
 * <li>The client is notified that the session id is no longer valid with
 * {@link HttpSessionIdResolver#expireSession(HttpServletRequest, HttpServletResponse)}
 * </li>
 * </ul>
 *
 * <p>
 * The SessionRepositoryFilter must be placed before any Filter that access the
 * HttpSession or that might commit the response to ensure the session is overridden and
 * persisted properly.
 * </p>
 */
@Order(TokenSessionRepositoryFilter.DEFAULT_ORDER)
public class TokenSessionRepositoryFilter<S extends Session> extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(TokenSessionRepositoryFilter.class.getName());

	/**
	 * The session repository request attribute name.
	 */
	public static final String SESSION_REPOSITORY_ATTR = TokenSessionRepository.class.getName();

	private static final String CURRENT_SESSION_ATTR = SESSION_REPOSITORY_ATTR + ".CURRENT_SESSION";

	/**
	 * The default filter order.
	 */
	public static final int DEFAULT_ORDER = Integer.MIN_VALUE + 50;

	private final TokenSessionRepository<S> sessionRepository;

	private TokenHttpSessionIdResolver httpSessionIdResolver = new TokenHttpSessionIdResolver();

	/**
	 * Creates a new instance.
	 * @param sessionRepository the <code>SessionRepository</code> to use. Cannot be null.
	 */
	public TokenSessionRepositoryFilter(TokenSessionRepository<S> sessionRepository) {
		if (sessionRepository == null) {
			throw new IllegalArgumentException("sessionRepository cannot be null");
		}
		this.sessionRepository = sessionRepository;
	}

	/**
	 * Sets the {@link HttpSessionIdResolver} to be used. The default is a
	 * {@link CookieHttpSessionIdResolver}.
	 * @param httpSessionIdResolver the {@link HttpSessionIdResolver} to use. Cannot be
	 * null.
	 */
	public void setHttpSessionIdResolver(TokenHttpSessionIdResolver httpSessionIdResolver) {
		if (httpSessionIdResolver == null) {
			throw new IllegalArgumentException("httpSessionIdResolver cannot be null");
		}
		this.httpSessionIdResolver = httpSessionIdResolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		request.setAttribute(SESSION_REPOSITORY_ATTR, this.sessionRepository);

		SessionRepositoryRequestWrapper wrappedRequest = new SessionRepositoryRequestWrapper(request, response);
		SessionRepositoryResponseWrapper wrappedResponse = new SessionRepositoryResponseWrapper(wrappedRequest,
				response);

		try {
			filterChain.doFilter(wrappedRequest, wrappedResponse);
		}
		finally {
			wrappedRequest.commitSession();
		}
	}

	@Override
	protected void doFilterNestedErrorDispatch(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		doFilterInternal(request, response, filterChain);
	}

	/**
	 * Allows ensuring that the session is saved if the response is committed.
	 *
	 * @author Rob Winch
	 * @since 1.0
	 */
	private final class SessionRepositoryResponseWrapper extends OnCommittedResponseWrapper {

		private final SessionRepositoryRequestWrapper request;

		/**
		 * Create a new {@link SessionRepositoryResponseWrapper}.
		 * @param request the request to be wrapped
		 * @param response the response to be wrapped
		 */
		SessionRepositoryResponseWrapper(SessionRepositoryRequestWrapper request, HttpServletResponse response) {
			super(response);
			if (request == null) {
				throw new IllegalArgumentException("request cannot be null");
			}
			this.request = request;
		}

		@Override
		protected void onResponseCommitted() {
			this.request.commitSession();
		}

	}

	/**
	 * A {@link HttpServletRequest} that retrieves the
	 * {@link HttpSession} using a
	 * {@link SessionRepository}.
	 *
	 * @author Rob Winch
	 * @since 1.0
	 */
	private final class SessionRepositoryRequestWrapper extends HttpServletRequestWrapper {

		private final HttpServletResponse response;

		private S requestedSession;

		private boolean requestedSessionCached;

		private String requestedSessionId;

		private Boolean requestedSessionIdValid;

		private boolean requestedSessionInvalidated;

		private boolean hasCommittedInInclude;

		private SessionRepositoryRequestWrapper(HttpServletRequest request, HttpServletResponse response) {
			super(request);
			this.response = response;
		}

		/**
		 * Uses the {@link HttpSessionIdResolver} to write the session id to the response
		 * and persist the Session.
		 */
		private void commitSession() {
			HttpSessionWrapper wrappedSession = getCurrentSession();
			if (wrappedSession == null) {
				if (isInvalidateClientSession()) {
					TokenSessionRepositoryFilter.this.httpSessionIdResolver.expireSession(this, this.response);
				}
			}
			else {
				S session = wrappedSession.getSession();
				String requestedSessionId = getRequestedSessionId();
				clearRequestedSessionCache();
				SessionRepositoryFilter.this.sessionRepository.save(session);
				String sessionId = session.getId();
				if (!isRequestedSessionIdValid() || !sessionId.equals(requestedSessionId)) {
					SessionRepositoryFilter.this.httpSessionIdResolver.setSessionId(this, this.response, sessionId);
				}
			}
		}

		@SuppressWarnings("unchecked")
		private HttpSessionWrapper getCurrentSession() {
			return (HttpSessionWrapper) getAttribute(CURRENT_SESSION_ATTR);
		}

		private void setCurrentSession(HttpSessionWrapper currentSession) {
			if (currentSession == null) {
				removeAttribute(CURRENT_SESSION_ATTR);
			}
			else {
				setAttribute(CURRENT_SESSION_ATTR, currentSession);
			}
		}

		@Override
		@SuppressWarnings("unused")
		public String changeSessionId() {
			HttpSession session = getSession(false);

			if (session == null) {
				throw new IllegalStateException(
						"Cannot change session ID. There is no session associated with this request.");
			}

			return getCurrentSession().getSession().changeSessionId();
		}

		@Override
		public boolean isRequestedSessionIdValid() {
			if (this.requestedSessionIdValid == null) {
				S requestedSession = getRequestedSession();
				if (requestedSession != null) {
					requestedSession.setLastAccessedTime(Instant.now());
				}
				this.requestedSessionIdValid = (requestedSession != null);
			}
			return this.requestedSessionIdValid;
		}

		private boolean isInvalidateClientSession() {
			return getCurrentSession() == null && this.requestedSessionInvalidated;
		}

		@Override
		public HttpSessionWrapper getSession(boolean create) {
			HttpSessionWrapper currentSession = getCurrentSession();
			if (currentSession != null) {
				return currentSession;
			}
			S requestedSession = getRequestedSession();
			if (requestedSession != null) {
				requestedSession.setLastAccessedTime(Instant.now());
				this.requestedSessionIdValid = true;
				currentSession = new HttpSessionWrapper(requestedSession, getServletContext());
				currentSession.markNotNew();
				setCurrentSession(currentSession);
				return currentSession;
			}

			if (!create) {
				return null;
			}

			if (logger.isDebugEnabled()) {
				logger.debug(
						"A new session was created. To help you troubleshoot where the session was created " +
								"we provided a StackTrace (this is not an error). You can prevent this from " +
								"appearing by disabling DEBUG logging for "
								+ TokenSessionRepositoryFilter.class.getName(),
						new RuntimeException("For debugging purposes only (not an error)"));
			}
			S session = TokenSessionRepositoryFilter.this.sessionRepository.createSession(this.requestedSessionId);
			session.setLastAccessedTime(Instant.now());
			currentSession = new HttpSessionWrapper(session, getServletContext());
			setCurrentSession(currentSession);
			return currentSession;
		}

		@Override
		public HttpSessionWrapper getSession() {
			return getSession(true);
		}

		@Override
		public String getRequestedSessionId() {
			if (this.requestedSessionId == null) {
				getRequestedSession();
			}
			return this.requestedSessionId;
		}

		@Override
		public RequestDispatcher getRequestDispatcher(String path) {
			RequestDispatcher requestDispatcher = super.getRequestDispatcher(path);
			return new SessionCommittingRequestDispatcher(requestDispatcher);
		}

		private S getRequestedSession() {
			if (!this.requestedSessionCached) {
				String sessionId = TokenSessionRepositoryFilter.this.httpSessionIdResolver.resolveSessionId(this);
				if (this.requestedSessionId == null) {
					this.requestedSessionId = sessionId;
				}
				S session = TokenSessionRepositoryFilter.this.sessionRepository.findById(sessionId);
				if (session != null) {
					this.requestedSession = session;
					this.requestedSessionCached = true;
				}
			}
			return this.requestedSession;
		}

		private void clearRequestedSessionCache() {
			this.requestedSessionCached = false;
			this.requestedSession = null;
			this.requestedSessionId = null;
		}

		/**
		 * Allows creating an HttpSession from a Session instance.
		 *
		 * @author Rob Winch
		 * @since 1.0
		 */
		private final class HttpSessionWrapper extends HttpSessionAdapter<S> {

			HttpSessionWrapper(S session, ServletContext servletContext) {
				super(session, servletContext);
			}

			@Override
			public void invalidate() {
				super.invalidate();
				SessionRepositoryRequestWrapper.this.requestedSessionInvalidated = true;
				setCurrentSession(null);
				clearRequestedSessionCache();
				TokenSessionRepositoryFilter.this.sessionRepository.deleteById(getId());
			}

		}

		/**
		 * Ensures session is committed before issuing an include.
		 *
		 * @since 1.3.4
		 */
		private final class SessionCommittingRequestDispatcher implements RequestDispatcher {

			private final RequestDispatcher delegate;

			SessionCommittingRequestDispatcher(RequestDispatcher delegate) {
				this.delegate = delegate;
			}

			@Override
			public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
				this.delegate.forward(request, response);
			}

			@Override
			public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
				if (!SessionRepositoryRequestWrapper.this.hasCommittedInInclude) {
					SessionRepositoryRequestWrapper.this.commitSession();
					SessionRepositoryRequestWrapper.this.hasCommittedInInclude = true;
				}
				this.delegate.include(request, response);
			}

		}

	}

}
