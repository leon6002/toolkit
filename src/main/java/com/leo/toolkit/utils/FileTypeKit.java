package com.leo.toolkit.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.util.HashMap;

@Slf4j
public class FileTypeKit {

    /**
     * 类描述：获取和判断⽂件头信息
     * |--⽂件头是位于⽂件开头的⼀段承担⼀定任务的数据，⼀般都在开头的部分。
     * |--头⽂件作为⼀种包含功能函数、数据接⼝声明的载体⽂件，⽤于保存程序的声明(declaration),⽽定义⽂件⽤于保存程序的实现(implementation)。
     * |--为了解决在⽤户上传⽂件的时候在服务器端判断⽂件类型的问题，故⽤获取⽂件头的⽅式，直接读取⽂件的前⼏个字节，来判断上传⽂件是否符合格式。 *
     */

    // 缓存⽂件头信息-⽂件头信息
    public static final HashMap<String, String> mFileTypes = new HashMap<>();

    static {
        mFileTypes.put("FFD8FF", "jpg");
        mFileTypes.put("89504E47", "png");
        mFileTypes.put("47494638", "gif");
        mFileTypes.put("49492A00", "tif");
        mFileTypes.put("424D", "bmp");
        mFileTypes.put("41433130", "dwg"); // CAD
        mFileTypes.put("38425053", "psd");
        mFileTypes.put("7B5C727466", "rtf"); // ⽇记本
        mFileTypes.put("3C3F786D6C", "xml");
        mFileTypes.put("68746D6C3E", "html");
        mFileTypes.put("44656C69766572792D646174653A", "eml"); // 邮件
        mFileTypes.put("D0CF11E0", "doc");
        mFileTypes.put("D0CF11E0", "xls");//excel2003版本⽂件
        mFileTypes.put("5374616E64617264204A", "mdb");
        mFileTypes.put("252150532D41646F6265", "ps");
        mFileTypes.put("255044462D312E", "pdf");
        mFileTypes.put("504B0304", "docx");
        mFileTypes.put("504B0304", "xlsx");//excel2007以上版本⽂件
        mFileTypes.put("52617221", "rar");
        mFileTypes.put("57415645", "wav");
        mFileTypes.put("41564920", "avi");
        mFileTypes.put("2E524D46", "rm");
        mFileTypes.put("000001BA", "mpg");
        mFileTypes.put("000001B3", "mpg");
        mFileTypes.put("6D6F6F76", "mov");
        mFileTypes.put("3026B2758E66CF11", "asf");
        mFileTypes.put("4D546864", "mid");
        mFileTypes.put("1F8B08", "gz");
    }

    /**
     * @param filePath ⽂件路径
     * @return ⽂件头信息
     * @author guoxk
     * <p>
     * ⽅法描述：根据⽂件路径获取⽂件头信息
     */
    public static String getFileType(String filePath) {
        String fileHeader = getFileHeader(filePath);
        log.info("fileHeader: {}", fileHeader);
        return mFileTypes.get(fileHeader);
    }

    /**
     * @param filePath ⽂件路径
     * @return ⽂件头信息
     * @author guoxk
     * <p>
     * ⽅法描述：根据⽂件路径获取⽂件头信息
     */
    public static String getFileHeader(String filePath) {
        String value = null;
        try (FileInputStream is = new FileInputStream(filePath)) {
            byte[] b = new byte[4];
            /*
             * int read() 从此输⼊流中读取⼀个数据字节。
             * int read(byte[] b) 从此输⼊流中将最多 b.length个字节的数据读⼊⼀个 byte 数组中。
             * int read(byte[] b, int off, int len)从此输⼊流中将最多 len 个字节的数据读⼊⼀个 byte 数组中。
             */
            int read = is.read(b, 0, b.length);
            log.info("read: {}", read);
            value = bytesToHexString(b);
        } catch (Exception e) {
            log.error("获取文件头出错: ", e);
        }
        return value;
    }

    /**
     * @param src 要读取⽂件头信息的⽂件的byte数组
     * @return ⽂件头信息
     * @author guoxk
     * <p>
     * ⽅法描述：将要读取⽂件头信息的⽂件的byte数组转换成string类型表⽰
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (byte b : src) {
            // 以⼗六进制（基数 16）⽆符号整数形式返回⼀个整数参数的字符串表⽰形式，并转换为⼤写
            hv = Integer.toHexString(b & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }

    /**
     * @param args
     * @throws Exception
     * @author guoxk
     * <p>
     * ⽅法描述：测试
     */
    public static void main(String[] args) throws Exception {
        final String fileType = getFileType("/Users/apple/Documents/test_resources/Java开发手册-泰山版.pdf");
        System.out.println(fileType);
    }
}
