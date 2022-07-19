package com.wugang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Category {
    /**
     * id
     */
    private String id;

    /**
     * 父节点id
     */
    private String parent;

    /**
     * 名称
     */
    private String name;

    /**
     * 排序
     */
    private int sort;
}
