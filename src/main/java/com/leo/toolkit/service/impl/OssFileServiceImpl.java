package com.leo.toolkit.service.impl;

import com.aliyun.oss.OSS;
import com.leo.toolkit.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class OssFileServiceImpl implements FileService {

    @Value("${oss.bucket-name.l-gen}")
    private String bucketNameGen;
    @Value("${oss.bucket-url.l-gen}")
    private String bucketUrlGen;
    @Autowired
    private OSS ossClient;


    @Override
    public String upload(byte[] bytes, String fileName) {
        return upload(new ByteArrayInputStream(bytes), fileName);
    }

    @Override
    public void download(String origin, String dest) {
//        ossProcess(ossClient -> {
//            ossClient.getObject(new GetObjectRequest(bucketName, origin), new File(dest));
//        });
    }
//    @Override
//    public void upload(InputStream is, String filename) {
//        Date now = new Date();
//        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
//        String key = "toolkit/" + fmt.format(now) + '/' + now.getTime() + "/" + filename;
//        ossProcess(ossClient -> {
//            ossClient.putObject(bucketName, key, is);
//        });
//    }

    @Override
    public String upload(InputStream is, String filename) {
        Date now = new Date();
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String key = "toolkit/" + fmt.format(now) + '/' + now.getTime() + "/" + filename;
        log.debug("uploading:{}", key);
        ossClient.putObject(bucketNameGen, key, is);
        log.debug("upload complete:{}", key);
        return bucketUrlGen + key;
    }


}
