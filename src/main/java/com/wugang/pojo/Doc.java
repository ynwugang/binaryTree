package com.wugang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doc {

    /**
     * id
     */
    private String id;

    /**
     * 电子书ID
     */
    private String ebookId;

    /**
     * 父节点ID
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
     * 阅读数
     */
    private int viewCount;

    /**
     * 点赞数
     */
    private int voteCount;
}
