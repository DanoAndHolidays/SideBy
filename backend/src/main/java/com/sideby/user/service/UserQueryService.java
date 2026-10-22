package com.sideby.user.service;

import com.sideby.user.entity.AppUser;
import com.sideby.user.mapper.AppUserMapper;
import com.sideby.user.vo.CurrentUserVO;
import org.springframework.stereotype.Service;

@Service
public class UserQueryService {
    private final AppUserMapper appUserMapper;

    public UserQueryService(AppUserMapper appUserMapper) {
        this.appUserMapper = appUserMapper;
    }

    public CurrentUserVO findCurrentUser(String username){
        AppUser appUser = appUserMapper.findByIdentifier(username);

        if(appUser ==  null){
            throw new IllegalStateException("Authenticated user was not found");
        }

        return new CurrentUserVO(
            appUser.getId().toString(),
            appUser.getUsername(),
            appUser.getEmail(),
            appUser.getDisplayName(),
            appUser.getAvatarUrl(),
            appUser.getBio()
        );
    }
}
