package com.leo.toolkit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZipFileInfo {
    private String filename;
    private String url;
    private Long size;
}
