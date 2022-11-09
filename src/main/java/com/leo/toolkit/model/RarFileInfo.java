package com.leo.toolkit.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RarFileInfo {

    private String fileName;

    private String url;

    private Long size;
}
