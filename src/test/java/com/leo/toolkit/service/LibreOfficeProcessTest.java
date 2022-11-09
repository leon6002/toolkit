package com.leo.toolkit.service;

import com.leo.toolkit.utils.FileKit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LibreOfficeProcessTest {

    @Autowired
    LibreOfficeProcess libreOfficeProcess;

    @Test
    void convertPdf() throws Exception {
        String url = "/Users/apple/Documents/test_resources/海康入场人员信息test.xls";
        String s = libreOfficeProcess.convertPdf(url);
        System.out.println("final s:" + s);
    }
}