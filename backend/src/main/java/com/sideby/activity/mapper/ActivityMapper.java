package com.sideby.activity.mapper;

import com.sideby.activity.dto.ActivityQuery;
import com.sideby.activity.entity.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityMapper {
    int insert(Activity activity);
    Activity findPublishedById(Long id);
    Activity findById(Long id);
    List<Activity> findPublishedPage(ActivityQuery query, int offset, int limit);
    long countPublished(ActivityQuery query);
    int updateById(Activity activity);
    int cancelById(@Param("id") Long id, @Param("creatorId") Long creatorId);
}
