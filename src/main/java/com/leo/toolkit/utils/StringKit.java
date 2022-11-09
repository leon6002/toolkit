package com.leo.toolkit.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringKit {
    public static void main(String[] args) {
        String fileExt = getFileExt("abc.pptx");
        System.out.println(fileExt);
    }

    public static String getFileExt(String filename) {
        String[] cuts = filename.split("\\.");
        if (cuts.length <= 1) {
            return "";
        }
        return cuts[cuts.length - 1];
    }
}
