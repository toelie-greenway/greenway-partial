package com.greenwaymyanmar.common.data.api.v1.response;

import com.google.gson.annotations.SerializedName;
import com.greenwaymyanmar.common.data.api.v1.Pagination;

import java.util.List;

import greenway_myanmar.org.vo.Thread;

public class ThreadListResponse {

    @SerializedName("pagination")
    private Pagination pagination;

    @SerializedName("data")
    private List<Thread> threads;

    public Pagination getPagination() {
        return pagination;
    }

    public List<Thread> getThreads() {
        return threads;
    }
}
