package com.wugang.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private long total;

    private List<T> list;

    @Override
    public String toString() {
        return "PageResponse{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
