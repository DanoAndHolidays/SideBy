package com.sideby.system.controller;

import com.sideby.common.api.ApiResponse;
import com.sideby.system.service.SystemStatusService;
import com.sideby.system.vo.SystemStatusVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
public class SystemStatusController {

    private final SystemStatusService systemStatusService;

    public SystemStatusController(SystemStatusService systemStatusService) {
        this.systemStatusService = systemStatusService;
    }

    @GetMapping("/status")
    public ApiResponse<SystemStatusVO> getStatus() {
        return ApiResponse.success(systemStatusService.getStatus());
    }
}

