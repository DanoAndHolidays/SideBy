package com.sideby.activity.controller;

import com.sideby.activity.dto.CreateActivityCommand;
import com.sideby.activity.dto.UpdateActivityCommand;
import com.sideby.activity.vo.ActivityDetailVO;
import com.sideby.common.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/activies")
public class ActivityController {
    @GetMapping("/{activityId}")
    public ResponseEntity<ApiResponse<ActivityDetailVO>> getActivity(Long activityId) {
        return null;
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<ActivityDetailVO>> createActivity(@RequestBody CreateActivityCommand createActivityCommand) {
        return null;
    }

    @PutMapping("/{activityId}")
    public ResponseEntity<ApiResponse<ActivityDetailVO>> updateActivity(Long activityId, @RequestBody UpdateActivityCommand updateActivityCommand) {
        return null;
    }

    @PostMapping("/{activityId}/cancel")
    public ResponseEntity<ApiResponse<ActivityDetailVO>> createActivity(Long activityId) {
        return null;
    }
}
