package com.sideby.activity.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CreateActivityCommand {
    private String title;
    private String description;
    private String category;
    private String city;
    private String locationName;
    private String locationAddress;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime registrationDeadline;
    private Integer capacity;
}
