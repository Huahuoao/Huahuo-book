package com.huahuo.huahuobook.utils;

import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;

public class FileUtil {

    // 图片允许的后缀扩展名
    public static String[] IMAGE_FILE_EXTD = new String[] { "png", "bmp", "jpg", "jpeg","pdf","mp4" };

    public static boolean isFileAllowed(String fileName) {
        for (String ext : IMAGE_FILE_EXTD) {
            if (ext.equals(fileName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 文件File类型转BASE64
     *
     * @param
     */
    public static String multipartFileToBASE64(MultipartFile mFile) throws Exception{
        BASE64Encoder bEncoder=new BASE64Encoder();
        String[] suffixAra=mFile.getOriginalFilename().split("\\.");
        String base64EncoderImg=bEncoder.encode(mFile.getBytes()).replaceAll("[\\s*\t\n\r]", "");
        return base64EncoderImg;
    }

    public static String fileToBase64(File file) {
        return Base64.getEncoder().encodeToString(fileToByte(file));
    }

    /**
     * 文件File类型转byte[]
     *
     * @param file
     * @return
     */
    public static byte[] fileToByte(File file) {
        byte[] fileBytes = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fileBytes = new byte[(int) file.length()];
            fis.read(fileBytes);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileBytes;
    }

}
