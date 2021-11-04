package com.cxhello.login.config.security;

import com.cxhello.login.constant.RedisKeyConstants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @author cxhello
 * @date 2021/11/2
 */
public class VerificationCodeAuthenticationProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private UserDetailsService userDetailsService;

    private StringRedisTemplate stringRedisTemplate;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    public VerificationCodeAuthenticationProvider(UserDetailsService userDetailsService, StringRedisTemplate stringRedisTemplate) {
        this.userDetailsService = userDetailsService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(userDetailsService, "userDetailsService must not be null");
        Assert.notNull(stringRedisTemplate, "stringRedisTemplate must not be null");
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(VerificationCodeAuthenticationToken.class, authentication,
                () -> this.messages.getMessage("VerificationCodeAuthenticationProvider.onlySupports",
                        "Only VerificationCodeAuthenticationToken is supported"));
        VerificationCodeAuthenticationToken verificationCodeAuthenticationToken = (VerificationCodeAuthenticationToken) authentication;
        String email = verificationCodeAuthenticationToken.getName();
        String verificationCode = verificationCodeAuthenticationToken.getCredentials().toString();
        UserDetails user = userDetailsService.loadUserByUsername(email);
        if (Objects.isNull(user)) {
            throw new BadCredentialsException("Bad credentials");
        }
        String resultCode = stringRedisTemplate.opsForValue().get(RedisKeyConstants.getUserCodeKey(email));
        Object principalToReturn = user;
        if (verificationCode.equals(resultCode)) {
            return createSuccessAuthentication(principalToReturn, authentication, user);
        } else {
            throw new BadCredentialsException("verificationCode is not matched");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (VerificationCodeAuthenticationToken.class.isAssignableFrom(authentication));
    }

    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
                                                         UserDetails user) {
        VerificationCodeAuthenticationToken result = new VerificationCodeAuthenticationToken(principal,
                authentication.getCredentials(), this.authoritiesMapper.mapAuthorities(user.getAuthorities()));
        result.setDetails(authentication.getDetails());
        return result;
    }

}
