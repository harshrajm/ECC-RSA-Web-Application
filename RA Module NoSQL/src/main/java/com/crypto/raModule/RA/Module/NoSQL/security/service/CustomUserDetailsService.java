package com.crypto.raModule.RA.Module.NoSQL.security.service;

import com.crypto.raModule.RA.Module.NoSQL.entities.User;
import com.crypto.raModule.RA.Module.NoSQL.security.CustomUserDetails;
import com.crypto.raModule.RA.Module.NoSQL.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //System.out.println("email "+s);
        User user =  userService.getUserByEmail(s);
        System.out.println(user);
        return new CustomUserDetails(user);
    }
}
