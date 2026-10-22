package com.sideby.auth.service;

import com.sideby.auth.dto.RegisterCommand;
import com.sideby.common.exception.BusinessException;
import com.sideby.user.entity.AppUser;
import com.sideby.user.mapper.AppUserMapper;
import com.sideby.user.vo.CurrentUserVO;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AppUserMapper appUserMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AppUserMapper appUserMapper, PasswordEncoder passwordEncoder) {
        this.appUserMapper = appUserMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public CurrentUserVO register(RegisterCommand registerCommand) {
        String username = registerCommand.getUsername().trim();
        String email = registerCommand.getEmail().trim().toLowerCase();
        String displayName = registerCommand.getDisplayName().trim();

        if (appUserMapper.countByUsername(username) > 0) {
            throw new BusinessException(
                    HttpStatus.CONFLICT,
                    "USERNAME_ALREADY_EXISTS",
                    "Username is already registered");
        }

        if (appUserMapper.countByEmail(email) > 0) {
            throw new BusinessException(
                    HttpStatus.CONFLICT,
                    "EMAIL_ALREADY_EXISTS",
                    "Email is already registered");
        }

        AppUser appUser = new AppUser();
        appUser.setEmail(email);
        appUser.setUsername(username);
        appUser.setPasswordHash(passwordEncoder.encode(registerCommand.getPassword()));
        appUser.setDisplayName(displayName);

        appUserMapper.insert(appUser);

        return toCurrentUserVO(appUser);
    }

    private CurrentUserVO toCurrentUserVO(AppUser appUser) {
        if (appUser.getId() == null) {
            throw new IllegalStateException("User id was not generated after insert");
        }

        return new CurrentUserVO(
                String.valueOf(appUser.getId()),
                appUser.getUsername(),
                appUser.getEmail(),
                appUser.getDisplayName(),
                appUser.getAvatarUrl(),
                appUser.getBio());
    }
}