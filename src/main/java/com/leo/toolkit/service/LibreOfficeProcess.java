package com.leo.toolkit.service;

import com.leo.toolkit.utils.FileKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.document.DocumentFormat;
import org.jodconverter.local.JodConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.Callable;

/**
 * 使用libreOffice实现文件转换成pdf的功能
 * 需要引入如下依赖
 * <dependency>
 * <groupId>org.jodconverter</groupId>
 * <artifactId>jodconverter-local</artifactId>
 * <version>4.4.2</version>
 * </dependency>
 * <p>
 * 还需要在本地安装libreOffice(windows, linux, macos均可安装对应版本安装成功后，这个程序在执行的时候会自动调用安装好的libreOffice)
 * 网址： https://zh-cn.libreoffice.org/download/libreoffice/
 */
@Slf4j
@Component
public class LibreOfficeProcess implements InitializingBean, DisposableBean {
    @Autowired
    FileService fileService;
    /**
     * 本地输出路径的文件夹地址
     */
    private static final String LOCAL_OUT_PUT_PATH = "/Users/apple/Documents/outputs/";
    private LocalOfficeManager manager;

    private String convertPdfFromUrl(String url) throws Exception {
        InputStream inputStream = FileKit.openStream(url);
        return convertPdf(inputStream);
    }

    private String convertPdfFromLocalPath(String filePath) throws Exception {
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        return convertPdf(fileInputStream);
    }

    /**
     * 将指定文件或者网页链接的内容转换为pdf
     *
     * @param url url链接或者本地路径， 如果http开头则下载文件并转换，否则直接当成本地文件打开并装欢
     * @return 生成的文件绝对路径， 目前本地设置的路径
     * @throws Exception exception
     */
    public String convertPdf(String url) throws Exception {
        if (url.startsWith("http")) {
            return convertPdfFromUrl(url);
        }
        return convertPdfFromLocalPath(url);
    }

    public String convertPdf(InputStream inputStream) throws Exception {
        Callable<String> callable = () -> {
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            JodConverter.convert(inputStream).to(out).as(DefaultDocumentFormatRegistry.XLSX).execute();
            String filename = System.currentTimeMillis() + "_" + RandomStringUtils.randomAlphabetic(6) + ".xlsx";
            String fullPath = LOCAL_OUT_PUT_PATH + filename;
            FileKit.writeFile(fullPath, out);
            return fullPath;
        };
        return callable.call();
    }

    /**
     * doc ppt xls 分别转换为 docx pptx xlsx
     * @param inputStream 文件输入流
     * @param fileName 文件名
     * @return 上传完成的文件地址
     */
    public String convertX(InputStream inputStream, String fileName) throws Exception {
        Callable<String> callable = () -> {
            String fileExt = FileKit.getFileExt(fileName);
            DocumentFormat documentFormat = null;
            if("xls".equalsIgnoreCase(fileExt)){
                documentFormat = DefaultDocumentFormatRegistry.XLSX;
            }
            if("ppt".equalsIgnoreCase(fileExt)){
                documentFormat = DefaultDocumentFormatRegistry.PPTX;
            }
            if("doc".equalsIgnoreCase(fileExt)){
                documentFormat = DefaultDocumentFormatRegistry.DOCX;
            }
            if(documentFormat==null){
                return null;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            JodConverter.convert(inputStream).to(out).as(documentFormat).execute();
            log.info("convert complete");
            return fileService.upload(out.toByteArray(), fileName + "x");
        };
        return callable.call();
    }

    @Override
    public void destroy() throws Exception {
        if (this.manager != null) {
            this.manager.stop();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.manager = LocalOfficeManager.builder().taskExecutionTimeout(360 * 1000L)
                .portNumbers(2002, 2003, 2004, 2005).maxTasksPerProcess(40).install().build();
        this.manager.start();
    }
}
