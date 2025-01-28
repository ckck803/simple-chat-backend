package org.example.simplechat.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

@NoArgsConstructor
@Getter
@Slf4j
public class UserDetailsImpl implements UserDetails {

    private String userId;
    private String username;
    private String email;
    private String password;
    private Set<GrantedAuthority> authorities;

    @Builder
    public UserDetailsImpl(String userId, String username, String email, String password, List<String> roleList) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = getAuthoritiesByRole(roleList);
    }

    private Set<GrantedAuthority> getAuthoritiesByRole(List<String> roleList) {
        Set<GrantedAuthority> grantedAuthorities = new TreeSet<>(new AuthoritiesComparator());
        roleList.forEach(role -> {
            if (!role.startsWith("ROLE_")) {
                role += "ROLE_";
            }
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        });

        return grantedAuthorities;
    }

    private class AuthoritiesComparator implements Comparator<GrantedAuthority>, Serializable {

        @Override
        public int compare(GrantedAuthority o1, GrantedAuthority o2) {
            if(o2.getAuthority() == null){
                return -1;
            }else{
               return o1.getAuthority() == null ? 1 : o1.getAuthority().compareTo(o2.getAuthority());
            }
        }

    }
    public String getUserId() {
        return this.userId;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
}
