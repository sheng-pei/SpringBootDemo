package org.example.springsession.login.reader;

import org.example.springsession.keys.KeyCenter;
import org.example.springsession.login.reader.random.RandomManager;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.DefaultSecurityFilterChain;

public class UnauthenticatedUsernamePasswordAuthenticationTokenReaderConfigurer<H extends HttpSecurityBuilder<H>>
        extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, H> {

    private UnauthenticatedUsernamePasswordAuthenticationTokenReader<H> tokenReader;

    @Override
    public void init(H builder) throws Exception {
        super.init(builder);
    }

    @Override
    public void configure(H builder) throws Exception {
        super.configure(builder);
        if (this.tokenReader == null) {
            KeyCenter keyCenter = getBeanOrNull(KeyCenter.class);
            RandomManager randomManager = getBeanOrNull(RandomManager.class);
            EncryptedUUPATReader<H> tokenReader = new EncryptedUUPATReader<>();
            tokenReader.setKeyCenter(keyCenter);
            tokenReader.setRandomManager(randomManager);
            this.tokenReader = tokenReader;
        }
        builder.setSharedObject(UnauthenticatedUsernamePasswordAuthenticationTokenReader.class, this.tokenReader);
        this.tokenReader.addFilters(builder);
    }

    public void setTokenReader(UnauthenticatedUsernamePasswordAuthenticationTokenReader<H> tokenReader) {
        this.tokenReader = tokenReader;
    }

    private <T> T getBeanOrNull(Class<T> type) {
        ApplicationContext context = getBuilder().getSharedObject(ApplicationContext.class);
        if (context == null) {
            return null;
        }
        try {
            return context.getBean(type);
        }
        catch (NoSuchBeanDefinitionException ex) {
            return null;
        }
    }
}
