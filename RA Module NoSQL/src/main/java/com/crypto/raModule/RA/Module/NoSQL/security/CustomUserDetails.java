package com.crypto.raModule.RA.Module.NoSQL.security;

import com.crypto.raModule.RA.Module.NoSQL.entities.User;
import com.crypto.raModule.RA.Module.NoSQL.helper.AppConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails extends User implements UserDetails {
    public CustomUserDetails(final User user){
        super(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        if(getRole() == AppConstants.ROLE_RA_OFFICER && isAdmin()){
            list.add(new SimpleGrantedAuthority("ROLE_RA_ADMIN"));
            }else if(getRole() == AppConstants.ROLE_RA_OFFICER && !isAdmin()){
            list.add(new SimpleGrantedAuthority("ROLE_RA_OFFICER"));
        }

        if(getRole() == AppConstants.ROLE_CA_OFFICER && isAdmin()){
            list.add(new SimpleGrantedAuthority("ROLE_CA_ADMIN"));
        }else if(getRole() == AppConstants.ROLE_CA_OFFICER && !isAdmin()){
            list.add(new SimpleGrantedAuthority("ROLE_CA_OFFICER"));
        }
        return list;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
