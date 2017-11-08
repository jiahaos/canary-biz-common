package com.canary.m.common.client;

import org.springframework.security.core.GrantedAuthority;

public class AccountGrantedAuthority implements GrantedAuthority {

    private String authority;

    public AccountGrantedAuthority(String authority){
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}