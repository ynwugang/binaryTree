package com.wugang.response;

import com.wugang.pojo.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryQueryResponse {
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

    /**
     * 子节点
     */
    private List<Category> children;
}
