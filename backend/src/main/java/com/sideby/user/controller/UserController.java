package com.sideby.user.controller;

import com.sideby.common.api.ApiResponse;
import com.sideby.user.service.UserQueryService;
import com.sideby.user.vo.CurrentUserVO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserQueryService userQueryService;

    public UserController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<CurrentUserVO>>me(Authentication authentication){

        CurrentUserVO user = userQueryService.findCurrentUser(authentication.getName());

        return ResponseEntity.ok(ApiResponse.success(user));
    }
}
