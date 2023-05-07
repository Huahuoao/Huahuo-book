package com.huahuo.huahuobook.controller;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataUnit;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huahuo.huahuobook.common.ResponseResult;
import com.huahuo.huahuobook.jsondto.VoiceResult;
import com.huahuo.huahuobook.jsondto.YyResult;
import com.huahuo.huahuobook.pojo.Bill;
import com.huahuo.huahuobook.pojo.Book;
import com.huahuo.huahuobook.service.BillService;
import com.huahuo.huahuobook.service.BookService;
import com.huahuo.huahuobook.utils.FileUtil;
import com.qiniu.util.Json;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import javax.xml.ws.Response;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @作者 花火
 * @创建日期 2023/1/29 16:09   测试用 测试用 测试用
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    BillService billService;
    @Autowired
    BookService bookService;


}
