package com.leo.toolkit.service;

import com.leo.toolkit.dto.ExtractDTO;

import java.io.IOException;

/**
 * 各种提取文字和图片的方法， 目前支持
 * HTML PDF WORD EXCEL PPT
 */
public interface ExtractService {
    /**
     * 提取文本里面的文字和图片
     *
     * @param url 网页链接或者本地文件地址，通过是否以http开头来判断是get url还是读取本地文件
     * @return ExtractDTO
     */
    ExtractDTO extract(String url) throws IOException;
}

