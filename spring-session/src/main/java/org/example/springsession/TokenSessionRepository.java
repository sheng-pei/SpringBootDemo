package org.example.springsession;

import org.springframework.session.Session;

public interface TokenSessionRepository<S extends Session> {
    S createSession(String id);

    void save(S session);

    S findById(String id);

    void deleteById(String id);
}