package com.huahuo.huahuobook.service;

import org.springframework.web.multipart.MultipartFile;

public interface QiniuService {
    public String saveImage(MultipartFile file);
    public String saveImage(byte[] bytes);
    public void deleteImg(String fileKey);
    public String getUpToken();
}
