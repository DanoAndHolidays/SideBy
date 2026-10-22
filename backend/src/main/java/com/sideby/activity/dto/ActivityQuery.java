package com.sideby.activity.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityQuery {
    //page        默认 1，最小 1
    //pageSize    默认 20，限制在 1 到 50
    //city        可选
    //category    可选
    //keyword     可选，匹配标题

    @Min(value = 1, message = "page must be at least 1")
    private int page = 1;

    @Min(value = 1, message = "pageSize must be at least 1")
    @Max(value = 50, message = "pageSize must not exceed 50")
    private int pageSize = 20;

    private String city;
    private String category;
    private String keyword;

    public ActivityQuery() {
    }

    public ActivityQuery(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public ActivityQuery(int page, int pageSize, String city, String category, String keyword) {
        this.page = page;
        this.pageSize = pageSize;
        this.city = city;
        this.category = category;
        this.keyword = keyword;
    }
}
