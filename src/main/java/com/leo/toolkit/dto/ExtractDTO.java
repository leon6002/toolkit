package com.leo.toolkit.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExtractDTO {
    /**
     * 提取出来的待检测文本
     */
    private String examineContent;
    /**
     * 提取出来的图片
     */
    private List<String> images;
}
