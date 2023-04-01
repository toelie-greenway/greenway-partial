package com.greenwaymyanmar.common.data.api.v1.response;

import com.google.gson.annotations.SerializedName;
import com.greenwaymyanmar.common.data.api.v1.Pagination;

import java.util.List;

import greenway_myanmar.org.vo.Post;

public class PostListResponse {

    @SerializedName("pagination")
    private Pagination pagination;

    @SerializedName("data")
    private List<Post> posts;

    public Pagination getPagination() {
        return pagination;
    }

    public List<Post> getPosts() {
        return posts;
    }
}
