package com.sideby.auth.controller;

import com.sideby.auth.dto.CsrfTokenResponse;
import com.sideby.auth.dto.LoginCommand;
import com.sideby.auth.dto.RegisterCommand;
import com.sideby.auth.service.AuthService;
import com.sideby.common.api.ApiResponse;
import com.sideby.user.entity.AppUser;
import com.sideby.user.mapper.AppUserMapper;
import com.sideby.user.vo.CurrentUserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final AppUserMapper appUserMapper;

    public AuthController(
        AuthService authService,
        AuthenticationManager authenticationManager,
        SecurityContextRepository securityContextRepository,
        AppUserMapper appUserMapper) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
        this.appUserMapper = appUserMapper;
    }

    @GetMapping("/csrf")
    public ResponseEntity<ApiResponse<CsrfTokenResponse>> csrf(CsrfToken csrfToken) {
        CsrfTokenResponse response = new CsrfTokenResponse(
            csrfToken.getHeaderName(),
            csrfToken.getToken());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<CurrentUserVO>> register(
        @Valid @RequestBody RegisterCommand registerCommand) {
        CurrentUserVO currentUser = authService.register(registerCommand);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(currentUser));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<CurrentUserVO>> login(
        @Valid @RequestBody LoginCommand command,
        HttpServletRequest request,
        HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    command.getIdentifier().trim(),
                    command.getPassword()
                )
            );

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, request, response);

            AppUser appUser = appUserMapper.findByUsername(authentication.getName());
            if (appUser == null) {
                throw new IllegalStateException("Authenticated user was not found");
            }

            return ResponseEntity.ok(ApiResponse.success(toCurrentUserVO(appUser)));
        } catch (AuthenticationException exception) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(
                    "INVALID_CREDENTIALS",
                    "Username/email or password is incorrect"));
        }
    }

    //@PostMapping("/logout")
    //public ResponseEntity<ApiResponse<CurrentUserVO>> logout(){
    //
    //    return null;
    //}

    private CurrentUserVO toCurrentUserVO(AppUser appUser) {
        return new CurrentUserVO(
            String.valueOf(appUser.getId()),
            appUser.getUsername(),
            appUser.getEmail(),
            appUser.getDisplayName(),
            appUser.getAvatarUrl(),
            appUser.getBio());
    }
}
