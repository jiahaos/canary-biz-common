package com.canary.m.common.client;

import com.canary.core.acl.AclAccountDTO;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by hsk on 2017/1/11.
 */
@Data
public class AccountUserDetails implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    private AclAccountDTO aclAccount;

    private int shopId;

    private boolean userCredentials;

    private boolean shopCredentials;

    public AccountUserDetails(AclAccountDTO aclAccount) {
        this.aclAccount = aclAccount;
    }

    public AccountUserDetails(AclAccountDTO aclAccount, int shopId, boolean userCredentials, boolean shopCredentials) {
        this.aclAccount = aclAccount;
        this.shopId = shopId;
        this.userCredentials = userCredentials;
        this.shopCredentials = shopCredentials;
    }

    // to shop certification and others

    public Integer getId() {
        return aclAccount.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Lists.transform(Lists.newArrayList("write", "read"), String -> new AccountGrantedAuthority(String));
    }

    @Override
    public String getPassword() {
        return aclAccount.getPassword();
    }

    @Override
    public String getUsername() {
        return aclAccount.getAccountName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return aclAccount.getLocked() == 0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return aclAccount.getDeleteStatus() == 0;
    }
}