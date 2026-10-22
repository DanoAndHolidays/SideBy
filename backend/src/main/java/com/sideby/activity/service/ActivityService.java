package com.sideby.activity.service;

import com.sideby.activity.dto.ActivityQuery;
import com.sideby.activity.dto.CreateActivityCommand;
import com.sideby.activity.dto.UpdateActivityCommand;
import com.sideby.activity.vo.ActivityDetailVO;
import com.sideby.activity.vo.ActivityPageVO;

public class ActivityService {
    public ActivityPageVO listPublished(ActivityQuery query){
        return null;
    };
    public ActivityDetailVO getPublishedDetail(Long activityId){
        return null;
    };
    public ActivityDetailVO create(Long currentUserId, CreateActivityCommand command){
        return null;
    };
    public ActivityDetailVO update(Long currentUserId, Long activityId, UpdateActivityCommand command){
        return null;
    };
    public void cancel(Long currentUserId, Long activityId){

    };
}
