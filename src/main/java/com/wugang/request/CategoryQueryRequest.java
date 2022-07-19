package com.wugang.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryQueryRequest{

    /**
     * id
     */
    private String id;

    /**
     * 父节点id
     */
    private String parent;
}
