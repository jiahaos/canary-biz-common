package com.canary.m.common.client;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by hsk on 2016/1/29.
 */
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