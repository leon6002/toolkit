package com.leo.toolkit.service.impl;

import com.leo.toolkit.consts.Const;
import com.leo.toolkit.dto.ExtractDTO;
import com.leo.toolkit.service.ExtractService;
import com.leo.toolkit.utils.FileKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 提取pdf中的图片和文字
 * 需如下依赖
 * <dependency>
 * <groupId>org.apache.pdfbox</groupId>
 * <artifactId>pdfbox</artifactId>
 * <version>2.0.26</version>
 * </dependency>
 */
@Slf4j
@Service("extractPdfService")
public class ExtractFromPdfServiceImpl implements ExtractService {

    /**
     * 提取文本里面的文字和图片
     *
     * @param url 网页链接或者本地文件地址，通过是否以http开头来判断是get url还是读取本地文件
     * @return ExtractDTO
     */
    @Override
    public ExtractDTO extract(String url) {
        url = Optional.ofNullable(url).orElse("");
        String examineContent = null;
        List<String> images = new ArrayList<>();
        try (InputStream is = FileKit.getInputStream(url)) {
            PDDocument document = PDDocument.load(is);
            ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
            Writer writer = new OutputStreamWriter(out);
            new PDFTextStripper().writeText(document, writer);
            writer.close();
            examineContent = out.toString();
            for (PDPage page : document.getPages()) {
                PDResources pdResources = page.getResources();
                for (COSName name : pdResources.getXObjectNames()) {
                    PDXObject o = pdResources.getXObject(name);
                    if (!(o instanceof PDImageXObject)) {
                        continue;
                    }
                    PDImageXObject imageXObject = (PDImageXObject) o;
                    String filename = RandomStringUtils.randomAlphabetic(6) + ".png";
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(imageXObject.getImage(), "png", baos);
                    String filePath = Const.LOCAL_OUTPUT_PATH + filename;
                    FileKit.writeFile(filePath, baos);
                    images.add(filePath);
                }
            }
            document.close();
        } catch (Exception e) {
            log.error("pdf提取文字和图片出错: ", e);
        }
        return new ExtractDTO(examineContent, images);
    }
}
