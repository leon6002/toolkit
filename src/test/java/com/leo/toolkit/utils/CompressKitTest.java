package com.leo.toolkit.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CompressKitTest {
    @Autowired
    CompressKit compressKit;
    @Test
    void uploadRar() {
        compressKit.uploadRar("/Users/apple/Documents/test_resources/rartestfolder.rar");
    }

    @Test
    void uploadZip() throws Exception {
        compressKit.uploadZip("/Users/apple/Documents/test_resources/rar test.zip");
    }
}