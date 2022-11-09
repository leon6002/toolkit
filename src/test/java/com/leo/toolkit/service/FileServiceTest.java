package com.leo.toolkit.service;

//import com.documents4j.api.DocumentType;
//import com.documents4j.api.IConverter;
//import com.documents4j.job.LocalConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

@SpringBootTest
class FileServiceTest {
    @Autowired
    FileService fileService;

    @Test
    void upload() throws FileNotFoundException {
        String uri = "/Volumes/SSK Storage/DJI_mavic_pro_platinum/安吉/DJI_0024.MOV";
        File file = new File(uri);
        String filename = file.getName();
        fileService.upload(new FileInputStream(file), filename);
    }

//    @Test
//    void download() {
//        File inputWord = new File("/Users/apple/Documents/test_resources/测试doc.doc");
//        File outputFile = new File("/Users/apple/Documents/test_resources/测试doc.docx");
//        try (InputStream docxInputStream = new FileInputStream(inputWord);
//             OutputStream outputStream = new FileOutputStream(outputFile)) {
//            IConverter converter = LocalConverter.builder().build();
//            boolean flag = false;
//            flag = converter.convert(docxInputStream).as(DocumentType.DOC).to(outputStream).as(DocumentType.DOCX).execute();
//            if (flag) {
//                converter.shutDown();
//            }
//            System.out.println("转换成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}