package com.sideby.activity.service;

import com.sideby.activity.dto.ActivityQuery;
import com.sideby.activity.dto.CreateActivityCommand;
import com.sideby.activity.dto.UpdateActivityCommand;
import com.sideby.activity.mapper.ActivityMapper;
import com.sideby.activity.mapper.row.ActivityDetailRow;
import com.sideby.activity.mapper.row.ActivitySummaryRow;
import com.sideby.activity.vo.ActivityDetailVO;
import com.sideby.activity.vo.ActivityPageVO;
import com.sideby.activity.vo.ActivitySummaryVO;
import com.sideby.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ActivityService {
    private final ActivityMapper activityMapper;

    public ActivityService(ActivityMapper activityMapper) {
        this.activityMapper = activityMapper;
    }

    public ActivityPageVO listPublished(ActivityQuery query) {
        int page = query.getPage();
        int pageSize = query.getPageSize();
        int offset = (page - 1) * pageSize;

        List<ActivitySummaryRow> rows = activityMapper.findPublishedPage(query, offset, pageSize);
        long total = activityMapper.countPublished(query);

        List<ActivitySummaryVO> items = rows.stream()
            .map(this::toSummaryVO)
            .toList();

        boolean hasNext = (long) offset + (long) items.size() < total;

        return new ActivityPageVO(items, page, pageSize, hasNext, total);
    }


    public ActivityDetailVO getPublishedDetail(Long activityId) {
        ActivityDetailRow row = activityMapper.findPublishedById(activityId);

        if (row == null) {
            throw new BusinessException(
                HttpStatus.NOT_FOUND,
                "ACTIVITY_NOT_FOUND",
                "Activity was not found"
            );
        }

        return toDetailVO(row);
    }


    public ActivityDetailVO create(Long currentUserId, CreateActivityCommand command) {

        activityMapper.insert(null);
        return null;
    }


    public ActivityDetailVO update(Long currentUserId, Long activityId, UpdateActivityCommand command) {

        activityMapper.updateById(null);
        return null;
    }


    public void cancel(Long currentUserId, Long activityId) {
        int i = activityMapper.cancelById(activityId, currentUserId);
        log.info("取消了 id 为 i 的活动{}", i);
    }

    // TODO 当前项目用 BeanUtils 自动复制普通同名字段，ID 手动转换；项目复杂后再换 MapStruct
    private ActivitySummaryVO toSummaryVO(ActivitySummaryRow row) {
        ActivitySummaryVO vo = new ActivitySummaryVO();

        vo.setId(row.getId().toString());
        vo.setCreatorId(row.getCreatorId().toString());
        vo.setCreatorDisplayName(row.getCreatorDisplayName());
        vo.setTitle(row.getTitle());
        vo.setCategory(row.getCategory());
        vo.setCity(row.getCity());
        vo.setLocationName(row.getLocationName());
        vo.setStartAt(row.getStartAt());
        vo.setEndAt(row.getEndAt());
        vo.setRegistrationDeadline(row.getRegistrationDeadline());
        vo.setCapacity(row.getCapacity());
        vo.setParticipantCount(row.getParticipantCount());
        vo.setStatus(row.getStatus());
        vo.setCoverUrl(row.getCoverUrl());

        return vo;
    }

    private ActivityDetailVO toDetailVO(ActivityDetailRow row) {
        ActivityDetailVO vo = new ActivityDetailVO();

        vo.setId(row.getId().toString());
        vo.setCreatorId(row.getCreatorId().toString());
        vo.setCreatorDisplayName(row.getCreatorDisplayName());
        vo.setCreatorUsername(row.getCreatorUsername());
        vo.setTitle(row.getTitle());
        vo.setDescription(row.getDescription());
        vo.setCategory(row.getCategory());
        vo.setCity(row.getCity());
        vo.setLocationName(row.getLocationName());
        vo.setLocationAddress(row.getLocationAddress());
        vo.setCoverUrl(row.getCoverUrl());
        vo.setStartAt(row.getStartAt());
        vo.setEndAt(row.getEndAt());
        vo.setRegistrationDeadline(row.getRegistrationDeadline());
        vo.setCapacity(row.getCapacity());
        vo.setParticipantCount(row.getParticipantCount());
        vo.setStatus(row.getStatus());
        vo.setCreatedAt(row.getCreatedAt());
        vo.setUpdatedAt(row.getUpdatedAt());

        return vo;
    }
}
