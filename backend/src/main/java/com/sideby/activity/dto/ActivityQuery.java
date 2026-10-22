package com.sideby.activity.dto;

import jakarta.validation.constraints.Size;
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

    @Size(min = 1)
    private int page;

    @Size(min = 1, max = 50, message = "pageSize must be between 1 and 50")
    private int getPage;

    private String city;
    private String category;
    private String keyword;

    public ActivityQuery(int page, int getPage) {
        this.page = page;
        this.getPage = getPage;
    }

    public ActivityQuery(int page, int getPage, String city, String category, String keyword) {
        this.page = page;
        this.getPage = getPage;
        this.city = city;
        this.category = category;
        this.keyword = keyword;
    }
}
