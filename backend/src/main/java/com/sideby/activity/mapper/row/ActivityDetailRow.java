package com.sideby.activity.mapper.row;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ActivityDetailRow {
    private Long id;
    private Long creatorId;
    private String creatorDisplayName;
    private String creatorUsername;
    private String title;
    private String description;
    private String category;
    private String city;
    private String locationName;
    private String locationAddress;
    private String coverUrl;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime registrationDeadline;
    private int capacity;
    private int participantCount;
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
