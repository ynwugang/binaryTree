package com.wugang.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocSaveRequest {

    /**
     * id
     */
    private String id;

    /**
     * 电子书ID
     */
    @NotNull(message = "【电子书】不能为空")
    private String ebookId;

    /**
     * 父节点ID
     */
    @NotNull(message = "【父文档】不能为空")
    private String parent;

    /**
     * 名称
     */
    @NotNull(message = "【文档名称】不能为空")
    private String name;

    /**
     * 排序
     */
    @NotNull(message = "【序号】不能为空")
    private int sort;

    /**
     * 阅读数
     */
    private int viewCount;

    /**
     * 点赞数
     */
    private int voteCount;

    /**
     * 文档内容
     */
    private String content;
}
