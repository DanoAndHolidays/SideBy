package com.sideby.activity.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ActivityPageVO {
    private List<ActivitySummaryVO> items;
    private int page;
    private int pageSize;
    private boolean hasNext;
    private long total;

    public ActivityPageVO() {
    }

    public ActivityPageVO(List<ActivitySummaryVO> items, int page, int pageSize, boolean hasNext, long total) {
        this.items = items;
        this.page = page;
        this.pageSize = pageSize;
        this.hasNext = hasNext;
        this.total = total;
    }
}
