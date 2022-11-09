package com.leo.toolkit.utils;


import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import com.leo.toolkit.model.RarFileInfo;
import com.leo.toolkit.model.ZipFileInfo;
import com.leo.toolkit.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Component
public class CompressKit {

    @Autowired
    FileService fileService;


//    /**
//     * 解压zip格式压缩包
//     * 对应的是ant.jar
//     */
//    private static void unzip(String sourceZip,String destDir) throws Exception{
//        try{
//            Project p = new Project();
//            Expand e = new Expand();
//            e.setProject(p);
//            e.setSrc(new File(sourceZip));
//            e.setOverwrite(false);
//            e.setDest(new File(destDir));
//           /*
//           ant下的zip工具默认压缩编码为UTF-8编码，
//           而winRAR软件压缩是用的windows默认的GBK或者GB2312编码
//           所以解压缩时要制定编码格式
//           */
//            e.setEncoding("gbk");
//            e.execute();
//        }catch(Exception e){
//            throw e;
//        }
//    }

    /**
     * 解压rar格式压缩包。
     * 对应的是java-unrar-0.3.jar，但是java-unrar-0.3.jar又会用到commons-logging-1.1.1.jar
     */
    private static void unrar(String sourceRar, String destDir) throws Exception {
        Archive a = null;
        FileOutputStream fos = null;
        try {
            a = new Archive(new File(sourceRar));
            FileHeader fh = a.nextFileHeader();
            while (fh != null) {
                if (!fh.isDirectory()) {
                    //1 根据不同的操作系统拿到相应的 destDirName 和 destFileName
                    String compressFileName = fh.getFileName().trim();
                    String destFileName = "";
                    String destDirName = "";
                    if (File.separator.equals("/")) {
                        //非windows系统
                        destFileName = destDir + compressFileName.replaceAll("\\\\", "/");
                        destDirName = destFileName.substring(0, destFileName.lastIndexOf("/"));
                    } else {
                        //window系统
                        destFileName = destDir + compressFileName.replaceAll("/", "\\\\");
                        destDirName = destFileName.substring(0, destFileName.lastIndexOf("\\"));
                    }
                    //2创建文件夹
                    File dir = new File(destDirName);
                    if (!dir.exists() || !dir.isDirectory()) {
                        dir.mkdirs();
                    }
                    //3解压缩文件
                    fos = new FileOutputStream(new File(destFileName));
                    a.extractFile(fh, fos);
                    fos.close();
                    fos = null;
                }
                fh = a.nextFileHeader();
            }
            a.close();
            a = null;
        } catch (Exception e) {
            throw e;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                    fos = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (a != null) {
                try {
                    a.close();
                    a = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void uploadRar(String sourceRar) {
        List<RarFileInfo> fileInfos = new ArrayList<>();
        try (Archive a = new Archive(new File(sourceRar))) {
            FileHeader fh = a.nextFileHeader();
            while (fh != null) {
                if (!fh.isDirectory()) {
                    String compressFileName = fh.getFileName().trim();
                    String[] split = compressFileName.replaceAll("\\\\", "/").split("/");
                    String fileName = split[split.length - 1];
                    try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                        a.extractFile(fh, bos);
                        String url = fileService.upload(bos.toByteArray(), fileName);
                        fileInfos.add(new RarFileInfo(fileName, url, (long) bos.size()));
                    }
                }
                fh = a.nextFileHeader();
            }
        } catch (Exception e) {
            log.error("error occured: ", e);
        }
        log.info("list:{}", JsonUtils.toJson(fileInfos));
    }

    public void uploadZip(String src) throws Exception {
        File file = new File(src);
        FileInputStream fileInputStream = new FileInputStream(file);
        uploadZip(fileInputStream, FileKit.getFileName(src));
    }

    private List<ZipFileInfo> uploadZip(InputStream inputStream, String filename) throws Exception {
        List<ZipFileInfo> zipFiles = new ArrayList<>();
        ZipInputStream zipIn = new ZipInputStream(inputStream, Charset.forName("GB2312"));
        BufferedInputStream bs = new BufferedInputStream(zipIn);
        ZipEntry entry;
        while (true) {
            try {
                if ((entry = zipIn.getNextEntry()) == null) {
                    break;
                }
                if (entry.isDirectory()) {
                    continue;
                }
                byte[] entryBytes;
                if (entry.getSize() == -1) {
                    entryBytes = IOUtils.toByteArray(bs);
                } else {
                    entryBytes = new byte[(int) entry.getSize()];
                    bs.read(entryBytes, 0, (int) entry.getSize());
                }
                try {
                    String url = fileService.upload(entryBytes, entry.getName());
                    zipFiles.add(new ZipFileInfo(entry.getName(), url, entry.getSize()));
                } catch (Exception e) {
                    log.error("upload zip entry:{} failed", entry.getName(), e);
                }
            } catch (IOException e) {
                log.error("error reading zip: {}", filename, e);
            } finally {
                try {
                    zipIn.closeEntry();
                } catch (IOException e) {
                    log.error("error closing zip: {}", filename, e);
                }
            }
        }
        try {
            bs.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return zipFiles;
    }

//    /**
//     * 解压缩
//     */
//    public static void deCompress(String sourceFile,String destDir) throws Exception{
//        //保证文件夹路径最后是"/"或者"\"
//        char lastChar = destDir.charAt(destDir.length()-1);
//        if(lastChar!='/'&&lastChar!='\\'){
//            destDir += File.separator;
//        }
//        //根据类型，进行相应的解压缩
//        String type = sourceFile.substring(sourceFile.lastIndexOf(".")+1);
//        if(type.equals("zip")){
//            unzip(sourceFile, destDir);
//        }else if(type.equals("rar")){
//            unrar(sourceFile, destDir);
//        }else{
//            throw new Exception("只支持zip和rar格式的压缩包！");
//        }
//    }


}
