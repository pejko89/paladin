package com.samsara.paladin.model.user;

import org.springframework.security.core.GrantedAuthority;

public enum PermissionEnum implements GrantedAuthority {

    READ,
    WRITE,
    UPDATE,
    DELETE;

    @Override
    public String getAuthority() {
        return name() + "_PERMISSION";
    }
}
