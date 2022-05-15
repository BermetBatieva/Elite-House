package com.example.HRMS.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ADMIN,
    RECRUITER,
    CANDIDATE,
    EMPLOYEE;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
