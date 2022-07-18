package com.example.retrofittest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCustomerRes {
    @SerializedName("info")
    @Expose
    private String info;
    @SerializedName("status")
    @Expose
    private Integer status;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
