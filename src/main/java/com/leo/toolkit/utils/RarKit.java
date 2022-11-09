//package com.leo.toolkit.utils;
//
//import net.sf.sevenzipjbinding.IInArchive;
//import net.sf.sevenzipjbinding.SevenZip;
//import net.sf.sevenzipjbinding.SevenZipException;
//import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
//
//import java.io.FileNotFoundException;
//import java.io.RandomAccessFile;
//
///**
// * <!-- https://mvnrepository.com/artifact/com.github.axet/java-unrar -->
// * 		<dependency>
// * 			<groupId>com.github.axet</groupId>
// * 			<artifactId>java-unrar</artifactId>
// * 			<version>1.7.0-8</version>
// * 		</dependency>
// * 		<dependency>
// * 			<groupId>net.sf.sevenzipjbinding</groupId>
// * 			<artifactId>sevenzipjbinding</artifactId>
// * 			<version>16.02-2.01</version>
// * 		</dependency>
// * 		<dependency>
// * 			<groupId>net.sf.sevenzipjbinding</groupId>
// * 			<artifactId>sevenzipjbinding-all-platforms</artifactId>
// * 			<version>16.02-2.01</version>
// * 		</dependency>
// */
//public class RarKit {
//    public static void main(String[] args) throws FileNotFoundException, SevenZipException {
//        String rarDir = "D:\\keys\\rar5.rar";
//        String outDir = "D:\\keys\\rar5";
//
//        RandomAccessFile randomAccessFile = null;
//        IInArchive inArchive = null;
//
//        // 第一个参数是需要解压的压缩包路径，第二个参数参考JdkAPI文档的RandomAccessFile
//        randomAccessFile = new RandomAccessFile(rarDir, "r");
//        inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));
//
//        int[] in = new int[inArchive.getNumberOfItems()];
//        for (int i = 0; i < in.length; i++) {
//            in[i] = i;
//        }
//        inArchive.extract(in, false, new ExtractCallback(inArchive, "366", outDir));
//    }
//}
