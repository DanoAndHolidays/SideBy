package com.sideby.activity.mapper.row;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ActivitySummaryRow {
    private Long id;
    private Long creatorId;
    private String creatorDisplayName;
    private String title;
    private String category;
    private String city;
    private String locationName;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime registrationDeadline;
    private int capacity;
    private int participantCount;
    private String status;
    private String coverUrl;
}
