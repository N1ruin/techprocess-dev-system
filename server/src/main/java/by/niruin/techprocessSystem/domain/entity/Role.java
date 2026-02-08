package by.niruin.techprocessSystem.domain.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ENGINEER,
    ROLE_ARCHIVE_MANAGER,
    ROLE_HEAD_OF_BUREAU,
    ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
