package com.huahuo.huahuobook.controller;

import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSONObject;
import com.huahuo.huahuobook.common.ResponseResult;
import com.huahuo.huahuobook.common.aop.LogAnnotation;
import com.huahuo.huahuobook.dto.Result;
import com.huahuo.huahuobook.pojo.Img;
import com.huahuo.huahuobook.service.ImgService;
import com.huahuo.huahuobook.service.QiniuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @作者 花火
 * @创建日期 2023/1/27 14:14
 */
@RestController
@RequestMapping
public class CommonController {
    static String appId = "3a21d9ab8504a4810fa886150414d7c9";
    // 请登录后前往 “工作台-账号设置-开发者信息” 查看 x-ti-secret-code
    // 示例代码中 x-ti-secret-code 非真实数据
    static String secretCode = "794af2a8c8ac71eaf25d2d7d1abc2aff";
    @Autowired
    QiniuService qiniuService;

    @Autowired
    private ImgService imgService;

    @PostMapping("/upload/img/bill") //先调用新建账单接口 获得id，再调用这个。
    @LogAnnotation(module = "common", operator = "上传图片")
    public ResponseResult uploadImg(@RequestParam("file") MultipartFile file, @RequestParam Integer id) {
        String url = qiniuService.saveImage(file);
        Img img = new Img();
        img.setSrc(url);
        img.setBillId(id);
        imgService.save(img);
        return ResponseResult.okResult("上传图片成功");
    }


    @PostMapping("/upload/img") //先调用新建账单接口 获得id，再调用这个。
    @LogAnnotation(module = "common", operator = "上传图片")
    public ResponseResult uploadImgCommon(@RequestParam("file") MultipartFile file) {
        String url = qiniuService.saveImage(file);
        return ResponseResult.okResult(url);
    }

    private static InputStream getImageStream(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                return inputStream;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @LogAnnotation(module = "common", operator = "剪切图片")
    @PostMapping("/cut/img")
    public Object test2(@RequestParam MultipartFile file) {
        // 文档图像切边矫正
        String url = "https://api.textin.com/ai/service/v1/dewarp";
        // 请登录后前往 “工作台-账号设置-开发者信息” 查看 x-ti-app-id
        // 示例代码中 x-ti-app-id 非真实数据

        BufferedReader in = null;
        DataOutputStream out = null;
        String result = "";
        try {
            byte[] imgData = file.getBytes(); // image
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("x-ti-app-id", appId);
            conn.setRequestProperty("x-ti-secret-code", secretCode);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST"); // 设置请求方式
            out = new DataOutputStream(conn.getOutputStream());
            out.write(imgData);
            out.flush();
            out.close();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
//        JSONObject json = JSONObject.parseObject(result);
//        Result result1 = json.getObject("result", Result.class);
//        byte[] decode = DatatypeConverter.parseBase64Binary(result1.getImage());
//        String s = qiniuService.saveImage(decode);
        return result;
    }


    @GetMapping("/download")
    public void downloadMultiFileToMinIO(HttpServletResponse response) throws IOException {
        //被压缩文件InputStream
        InputStream[] srcFiles = new InputStream[10];
        //被压缩文件名称
        String[] srcFileNames = new String[10];
        for (int i = 0; i < 10; i++) {
            InputStream inputStream = getImageStream("http://image.fzuhuahuo.cn/012224118284414197929ce072ebd0e0.png");
            if (inputStream == null) {
                continue;
            }
            srcFiles[i] = inputStream;
            srcFileNames[i] = "text" + i;
        }
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("下载测试.zip", "UTF-8"));
        //多个文件压缩成压缩包返回
        ZipUtil.zip(response.getOutputStream(), srcFileNames, srcFiles);
    }
    @LogAnnotation(module = "common", operator = "增强图片")
    @PostMapping("/enhance/img")
    public Object enhanceIMG(@RequestParam MultipartFile file) {

        String url = "https://api.textin.com/ai/service/v1/crop_enhance_image";
        BufferedReader in = null;
        DataOutputStream out = null;
        String result = "";
        try {
            byte[] imgData = file.getBytes(); // image
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("x-ti-app-id", appId);
            conn.setRequestProperty("x-ti-secret-code", secretCode);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST"); // 设置请求方式
            out = new DataOutputStream(conn.getOutputStream());
            out.write(imgData);
            out.flush();
            out.close();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;

    }

    public static byte[] readfile(String path) {
        String imgFile = path;
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

}

