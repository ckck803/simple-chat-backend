package org.example.simplechat.security.login.token;

import lombok.extern.slf4j.Slf4j;
import org.example.simplechat.security.dto.LoginRequest;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Slf4j
public class LoginAuthenticationToken extends AbstractAuthenticationToken {

    private Object principle;

    private Object credentials;

    public static LoginAuthenticationToken beforeAuthenticated(LoginRequest loginRequest){
        return new LoginAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
    }


    public static LoginAuthenticationToken beforeAuthenticated(Object principle, Object credentials){
        return new LoginAuthenticationToken(principle, credentials);
    }

    public static LoginAuthenticationToken authenticated(Object principle, Object credentials, Collection<? extends  GrantedAuthority> authorities){
        return new LoginAuthenticationToken(principle, credentials, authorities);
    }

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    private LoginAuthenticationToken(Object principle, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principle = principle;
        this.credentials = credentials;
        setAuthenticated(true);
    }

    private LoginAuthenticationToken(Object principle, Object credentials) {
        super(null);
        this.principle = principle;
        this.credentials = credentials;
        setAuthenticated(false);
    }


    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principle;
    }
}
