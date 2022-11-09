package com.leo.toolkit.service.impl;

import com.leo.toolkit.dto.ExtractDTO;
import com.leo.toolkit.service.ExtractService;
import com.leo.toolkit.utils.FileKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Slf4j
@Service
public class ExtractFromHtmlServiceImpl implements ExtractService {
    private static final Pattern IMAGE_PATTERN = Pattern.compile("<(img|image)[^>]*src=\"([^\"]*)\"[^>]*>", Pattern.CASE_INSENSITIVE);

    /**
     * 提取文本里面的文字和图片
     *
     * @param url 网页链接或者本地文件地址，通过是否以http开头来判断是get url还是读取本地文件
     * @return ExtractDTO
     */
    @Override
    public ExtractDTO extract(String url) throws IOException {
        StopWatch sw = new StopWatch();
        sw.start("start extracting");
        url = Optional.ofNullable(url).orElse("");
        InputStream inputStream = FileKit.getInputStream(url);
        String content = FileKit.is2String(inputStream);
        content = Optional.ofNullable(content).orElse("");
        String examineContent = content.replaceAll("<style[^>]*?>[\\s\\S]*?</style>", "")
                .replaceAll("</?[^>]+>", " ");
        Matcher matcher = IMAGE_PATTERN.matcher(content);
        List<String> images = new ArrayList<>();
        while (matcher.find()) {
            String src = HtmlUtils.htmlUnescape(matcher.group(2));
            images.add(src);
        }
        sw.stop();
        log.info("extract cost result {}", sw.prettyPrint());
        return new ExtractDTO(examineContent, images);
    }
}
