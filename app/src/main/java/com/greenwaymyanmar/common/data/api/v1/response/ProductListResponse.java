package com.greenwaymyanmar.common.data.api.v1.response;

import com.google.gson.annotations.SerializedName;
import com.greenwaymyanmar.common.data.api.v1.Pagination;

import java.util.List;

import greenway_myanmar.org.vo.Product;

public class ProductListResponse {

    @SerializedName("pagination")
    private Pagination pagination;

    @SerializedName("data")
    private List<Product> products;

    public Pagination getPagination() {
        return pagination;
    }

    public List<Product> getProducts() {
        return products;
    }
}
