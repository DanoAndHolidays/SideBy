package com.sideby.auth.security;

import com.sideby.user.entity.AppUser;
import com.sideby.user.mapper.AppUserMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private AppUserMapper appUserMapper;

    public AppUserDetailsService(AppUserMapper appUserMapper){
        this.appUserMapper = appUserMapper;
    }


    @Override
    public UserDetails loadUserByUsername(String identifier)throws UsernameNotFoundException {
        AppUser appUser = appUserMapper.findByIdentifier(identifier);

        if (appUser == null) {
            throw new UsernameNotFoundException("User not fond");
        }

        return User.withUsername(appUser.getUsername())
            .password(appUser.getPasswordHash())
            .roles("USER")
            .build();
    }
}
