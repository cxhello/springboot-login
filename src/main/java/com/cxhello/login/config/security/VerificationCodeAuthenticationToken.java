package com.cxhello.login.config.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * @author cxhello
 * @date 2021/11/2
 */
public class VerificationCodeAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final Object principal;

    private Object verificationCode;

    public VerificationCodeAuthenticationToken(Object principal, Object verificationCode) {
        super(null);
        this.principal = principal;
        this.verificationCode = verificationCode;
        setAuthenticated(false);
    }

    public VerificationCodeAuthenticationToken(Object principal, Object verificationCode,
                                               Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.verificationCode = verificationCode;
        super.setAuthenticated(true); // must use super, as we override
    }

    @Override
    public Object getCredentials() {
        return this.verificationCode;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated,
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.verificationCode = null;
    }

}
