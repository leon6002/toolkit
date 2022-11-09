package com.leo.toolkit.controller;

import com.leo.toolkit.enums.ResponseEnum;
import com.leo.toolkit.model.ResponseModel;
import com.leo.toolkit.service.FileService;
import com.leo.toolkit.service.LibreOfficeProcess;
import com.leo.toolkit.utils.FileKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("file")
@RestController
public class FileController {

    @Autowired
    FileService fileService;
    @Autowired
    LibreOfficeProcess libreOfficeProcess;


    @PostMapping("/upload")
    public ResponseModel<String> upload(@RequestParam("file") MultipartFile file, @RequestParam("filename") String filename) throws Exception {
        if (FileKit.isOldOffice(filename)) {
            String url = libreOfficeProcess.convertX(file.getInputStream(), filename);
            return new ResponseModel<>(url);
        }
        int fileType = FileKit.resolveContentType(filename);
        if (fileType == 0) {
            return new ResponseModel<>(ResponseEnum.FAIL);
        }
        String url = fileService.upload(file.getInputStream(), filename);
        return new ResponseModel<>(url);
    }

}
