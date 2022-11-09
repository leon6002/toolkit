package com.leo.toolkit.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * org.apache.http 这个依赖来自于
 * <dependency>
 * <groupId>com.aliyun.oss</groupId>
 * <artifactId>aliyun-sdk-oss</artifactId>
 * <version>3.15.1</version>
 * </dependency>
 */
public class FileKit {


    private static RestTemplate restTemplate = restTemplate();

    public static String is2String(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }

    public static String getFileName(String filePath) {
        filePath = Optional.ofNullable(filePath).orElse("");
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    public enum ContentTypeEnum {
        /**
         * HTML(1, "图文"),
         */
        HTML(1, "图文"),
        /**
         * IMAGE(2, "图片"),
         */
        IMAGE(2, "图片"),
        /**
         * 视频
         */
        VIDEO(3, "视频"),
        /**
         * PDF
         */
        PDF(4, "PDF"),

        /**
         * PPT
         */
        PPT(5, "PPT"),

        /**
         * WORD
         */
        WORD(6, "Word"),
        /**
         * EXCEL
         */
        EXCEL(7, "Excel"),
        /**
         * ZIP
         */
        ZIP(1000, "ZIP"),
        UNKNOWN(0, "未知");

        private final int code;
        private final String name;

        ContentTypeEnum(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static ContentTypeEnum of(int code) {
            for (ContentTypeEnum contentTypeEnum : ContentTypeEnum.values()) {
                if (Objects.equals(contentTypeEnum.getCode(), code)) {
                    return contentTypeEnum;
                }
            }
            return UNKNOWN;
        }

        public static String GetContentTypeName(Integer code) {
            if (code == null) {
                return null;
            }
            for (ContentTypeEnum contentTypeEnum : ContentTypeEnum.values()) {
                if (contentTypeEnum.getCode() == code) {
                    return contentTypeEnum.name;
                }
            }
            return null;
        }

        public boolean hasPdfPreview() {
            return this == PDF || this == WORD || this == PPT || this == EXCEL;
        }
    }

    public static Integer resolveContentType(String filename) {
        String ext = getFileExt(filename);
        if ("".equals(ext)) {
            return ContentTypeEnum.HTML.getCode();
        } else if ("mp4;m3u8;avi;mov".contains(ext)) {
            return ContentTypeEnum.VIDEO.getCode();
        } else if ("jpg;jpeg;gif;png".contains(ext)) {
            return ContentTypeEnum.IMAGE.getCode();
        } else if ("docx".equals(ext)) {
            return ContentTypeEnum.WORD.getCode();
        } else if ("pptx".equals(ext)) {
            return ContentTypeEnum.PPT.getCode();
        } else if ("xlsx".equals(ext)) {
            return ContentTypeEnum.EXCEL.getCode();
        } else if ("pdf".equals(ext)) {
            return ContentTypeEnum.PDF.getCode();
        } else if ("zip".equals(ext)) {
            return ContentTypeEnum.ZIP.getCode();
        } else {
            return ContentTypeEnum.UNKNOWN.getCode();
        }
    }

    /**
     * 根据文件名称判断是否是旧版office文件格式
     *
     * @param fileName 文件名称
     * @return boolean true-是旧版office false-不是旧版office
     */
    public static boolean isOldOffice(String fileName) {
        String[] oldExt = {"xls", "ppt", "doc"};
        return Arrays.stream(oldExt).anyMatch(e -> e.equalsIgnoreCase(getFileExt(fileName)));
    }


    public static InputStream openStream(String url) {
        try {
            ResponseEntity<Resource> entity = restTemplate().exchange(new URL(url).toURI(), HttpMethod.GET, null, Resource.class);
            return Objects.requireNonNull(entity.getBody()).getInputStream();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String getFileExt(String fileName) {
        String[] cuts = Optional.ofNullable(fileName).orElse("").split("\\.");
        return cuts.length <= 1 ? "" : cuts[cuts.length - 1].toLowerCase();
    }

    public static RestTemplate restTemplate() {
        try {
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(csf)
                    .build();
            HttpComponentsClientHttpRequestFactory requestFactory =
                    new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
            return new RestTemplate(requestFactory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeFile(String fileName, InputStream is) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            byte[] b = new byte[2048];
            while (is.read(b) != -1) {
                fos.write(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public static void writeFile(String fileName, ByteArrayOutputStream bos) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(bos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                bos.close();
            }
        }
    }

    public static String string2File(String destFolder, String str) {
        String fileName = RandomStringUtils.randomAlphabetic(6);
        String path = destFolder + fileName;
        try {
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(str);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return path;
    }

    public static InputStream getInputStream(String uri) throws FileNotFoundException {
        if (uri.startsWith("http")) {
            return openStream(uri);
        }
        return new FileInputStream(uri);
    }


    public static void main(String[] args) throws IOException {
//        Integer s = resolveContentType("*.");
//        System.out.println(s);
//        InputStream inputStream = openStream("https://whaleip.com");
//        writeFile("/Users/apple/Documents/test11111", inputStream);

        String fileName = getFileName("/adasdasd/uu.txt");
        System.out.println(fileName);
    }


}
