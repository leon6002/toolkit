package com.leo.toolkit.service;

import com.leo.toolkit.consts.Const;
import com.leo.toolkit.dto.ExtractDTO;
import com.leo.toolkit.utils.FileKit;
import com.leo.toolkit.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExtractServiceTest {

    @Autowired @Qualifier("extractPdfService")
    ExtractService extractService;

    @Test
    void extract() throws IOException {
        String path = Const.LOCAL_TEST_RESOURCE_PATH + "1664280311000诉讼服务协议.pdf";
        ExtractDTO extract = extractService.extract(path);
        String s = JsonUtils.toJson(extract);
        String file = FileKit.string2File(Const.LOCAL_OUTPUT_PATH, s);
        System.out.println(file);
    }
}