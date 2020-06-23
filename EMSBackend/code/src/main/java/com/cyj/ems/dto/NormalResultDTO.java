package com.cyj.ems.dto;

import java.io.Serializable;

public class NormalResultDTO implements Serializable {

    private Integer status;

    @Override
    public String toString() {
        return "NormalResultDTO{" +
                "status=" + status +
                '}';
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
