package com.leo.toolkit.service;

import java.io.InputStream;

public interface FileService {

    String upload(byte[] bytes, String fileName);

    void download(String origin, String dest);

    String upload(InputStream is, String filename);

}
