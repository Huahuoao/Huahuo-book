package com.huahuo.huahuobook.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.hash.Hash;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huahuo.huahuobook.common.ResponseResult;
import com.huahuo.huahuobook.common.aop.LogAnnotation;
import com.huahuo.huahuobook.dto.AIDto;
import com.huahuo.huahuobook.dto.HeHeJSON;
import com.huahuo.huahuobook.dto.HeHeTaxi;
import com.huahuo.huahuobook.dto.HeHeTrain;
import com.huahuo.huahuobook.jsondto.VoiceResult;
import com.huahuo.huahuobook.pojo.Bill;
import com.huahuo.huahuobook.pojo.Book;
import com.huahuo.huahuobook.pojo.User;
import com.huahuo.huahuobook.service.BillService;
import com.huahuo.huahuobook.service.BookService;
import com.huahuo.huahuobook.service.QiniuService;
import com.huahuo.huahuobook.service.UserService;
import com.huahuo.huahuobook.utils.FileUtil;
import org.apache.tomcat.jni.Multicast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @作者 花火
 * @创建日期 2023/3/20 19:42
 */
@RequestMapping("/ai")
@RestController
public class AIController {
    @Autowired
    UserService userService;
    @Autowired
    BookService bookService;
    @Autowired
    BillService billService;
    @Autowired
    QiniuService qiniuService;
    private static final String appId = "3a21d9ab8504a4810fa886150414d7c9";
    private static final String secretCode = "794af2a8c8ac71eaf25d2d7d1abc2aff";

    @PostMapping("/text/1")  //获得预算分配推荐
    @LogAnnotation(module = "ai", operator = "预算分配")
    public ResponseResult getText1(@RequestBody AIDto dto) {
        User user = userService.getById(dto.getUserId());
        Integer type = user.getAgeType(); //青年 中年 老年
        Double[] n = new Double[10];
        n[0] = 0.0;
        String prefix = null;
        switch (type) {
            case 1:
                n[1] = 0.3;
                n[2] = 0.05;
                n[3] = 0.1;
                n[4] = 0.1;
                n[5] = 0.15;
                n[6] = 0.05;
                n[7] = 0.1;
                n[8] = 0.05;
                prefix = "考虑到您的年龄，小秘希望主人可以多加学习，于是”文化教育“预算分配较多。同时希望主人能够天天向上，努力学习奋斗！";
                break;
            case 2:
                n[1] = 0.2;
                n[2] = 0.1;
                n[3] = 0.1;
                n[4] = 0.05;
                n[5] = 0.05;
                n[6] = 0.05;
                n[7] = 0.25;
                n[8] = 0.1;
                prefix = "小秘考虑到主人要经营家庭的生活，于是建议主人将更多的预算放在了”居家生活“中，同时也不要存下一点存款哦";

                break;
            case 3:
                n[1] = 0.25;
                n[2] = 0.05;
                n[3] = 0.05;
                n[4] = 0.05;
                n[5] = 0.25;
                n[6] = 0.1;
                n[7] = 0.1;
                n[8] = 0.05;
                prefix = "考虑到主人年龄状况，小秘希望主人将更多的钱投入到“健康医疗”中，同时注意适当锻炼，维持身体健康哦！";
                break;
        }
        Book book = bookService.getById(user.getDefaultBookId());
        DateTime dateTime = DateUtil.lastMonth();
        DateTime date = DateUtil.date();
        Double[] num = new Double[10];
        num[0] = book.getBudget();
        //0 预算 1吃 2交通 3玩 4购物 5文化 6药 7住房 8生活 9存款
        LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(Bill::getCreateTime, dateTime, date)
                .eq(Bill::getTypeOne, 1);
        billService.list(queryWrapper);
        num[1] = Math.rint(num[0] * n[1]);
        num[2] = Math.rint(num[0] * n[2]);
        num[3] = Math.rint(num[0] * n[3]);
        num[4] = Math.rint(num[0] * n[4]);
        num[5] = Math.rint(num[0] * n[5]);
        num[6] = Math.rint(num[0] * n[6]);
        num[7] = Math.rint(num[0] * n[7]);
        num[8] = Math.rint(num[0] * n[8]);
        num[9] = Math.rint(book.getBudget() - num[1]
                - num[2] - num[3] - num[4] - num[5] - num[6] - num[7] - num[8]
        );
//        String result = "您好" + user.getNickName() + "！我是帐小秘，综合考虑主人各种情况，小秘给主人拟了如下的预算分配: \n" +
//                "食品餐饮：" + num[1] + "元，这将包括日常食品、外出就餐、咖啡、酒吧等费用。\n" +
//                "出行交通：" + num[2] + "元，这将包括公共交通、出租车、汽车维护和保险等费用。\n" +
//                "休闲娱乐：" + num[3] + "元，这将包括电影、音乐、旅游和其他娱乐活动的费用。\n" +
//                "购物消费：" + num[4] + "元，这将包括购买日常用品、服装、家居用品等费用。\n" +
//                "文化教育：" + num[5] + "元，这将包括书籍、音乐、电影、博物馆门票、艺术展览门票等费用。\n" +
//                "健康医疗：" + num[6] + "元，这将包括医疗保险、药品费用、医疗检查等费用。\n" +
//                "酒店住宿：" + num[7] + "元，这将包括旅游期间住宿的费用。\n" +
//                "居家生活：" + num[8] + "元，这将包括家庭用品、电费、水费等费用。\n" +
//                "存款：" + num[9] + "元，这将是你的储蓄计划，可以用于应急和未来的投资。\n" +
//                "最后，你需要持续跟踪和管理你的预算，以确保你的支出和收入保持平衡，并做出必要的调整。\n"
//                + prefix;
        String result = "";
        HashMap<String, Object> res = new HashMap<>();
        res.put("给吴恩民的提示", "num[0]是总预算别管。num[1]到num[8]分别代表了 食品 出行 娱乐 购物 文化 健康 住宿 生活，然后存款是num[9]，但是这个存款体现在文本里就好,就是那个，您将有xxx元用于储蓄");
        res.put("array", num);
        return ResponseResult.okResult(res);
    }

    String getToken() {
        String s = HttpUtil.post("https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=Gy0zofHy4ytSU9IZR5rSipbB&client_secret=ZsWTpVavypKpH3Z59OtUkjDwxIXQ6LvT&", "");
        JSONObject json = JSON.parseObject(s);
        return json.getString("access_token");
    }
    @LogAnnotation(module = "ai", operator = "语音记账")
    @CacheEvict(value = "bill", allEntries = true)
    @RequestMapping("/voice")
    public ResponseResult t(@RequestParam("file") MultipartFile file) throws Exception {
        long size = file.getSize();
        String s = FileUtil.multipartFileToBASE64(file);
        HashMap<String, Object> body = new HashMap<>();
        body.put("format", "wav");
        body.put("rate", 16000);
        body.put("dev_pid", 80001);
        body.put("channel", 1);
        body.put("token", getToken());
        body.put("cuid", "baidu_workshop");
        body.put("len", size);
        body.put("speech", s);
        String s1 = JSONObject.toJSONString(body);
        String result2 = HttpRequest.post("https://vop.baidu.com/pro_api")
                .body(s1, "application/json")//表单内容
                .timeout(20000)//超时，毫秒
                .execute().body();
        JSONObject jsonObject5 = new JSONObject();
        try {
            jsonObject5 = JSONObject.parseObject(result2);
        } catch (Exception e) {
            return ResponseResult.errorResult(301, "识别失败");
        }
        JSONArray jsonArray = jsonObject5.getJSONArray("result");
        String text = (String) jsonArray.get(0);
        HashMap<String, Object> body2 = new HashMap<>();
        body2.put("text", text);
        String s2 = JSONObject.toJSONString(body2);
        String result3 = HttpRequest.post("http://81.68.194.42:3389/api/text/msg")
                .body(s2, "application/json")//表单内容
                .timeout(20000)//超时，毫秒
                .execute().body();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = JSONObject.parseObject(result3);
        } catch (Exception e) {
            return ResponseResult.errorResult(301, "识别失败");
        }
        System.out.println(jsonObject);
        JSONObject jsonObject2 = jsonObject.getJSONObject("result");
        VoiceResult voiceResult = jsonObject2.toJavaObject(VoiceResult.class);
        String date = voiceResult.getDate();
        Integer typeOne = voiceResult.getMoney() > 0 ? 2 : 1;
        Double money = Math.abs(voiceResult.getMoney());
        String itemType = jsonObject.getJSONObject("result").getJSONArray("itemType").getString(0);
        String typeTwo = "";
        if (itemType.equals("food")) {
            typeTwo = "食品餐饮";
        }
        if (itemType.equals("traffic")) {
            typeTwo = "出行交通";
        }
        if (itemType.equals("education")) {
            typeTwo = "文化教育";
        }
        if (itemType.equals("entertainment")) {
            typeTwo = "休闲娱乐";
        }
        if (itemType.equals("no type")) {
            typeTwo = "其他";
        }
        Bill bill = new Bill();
        bill.setText(text);
        bill.setNum(money);
        bill.setTypeOne(typeOne);
        bill.setTypeTwo(typeTwo);
        bill.setCreateTime(date);
        bill.setTypeThree(4);
        return ResponseResult.okResult(bill);
    }


    public JSONObject sendRequest(String url, byte[] imgData) throws IOException, ProtocolException {
        String result = "";
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "application/octet-stream");
        conn.setRequestProperty("x-ti-app-id", appId);
        conn.setRequestProperty("x-ti-secret-code", secretCode);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.write(imgData);
        out.flush();
        out.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        while (true) {
            String line = in.readLine();
            if (line == null) {
                return JSONObject.parseObject(result);
            }
            result = result + line;
        }
    }

    @PostMapping({"/ocr/v2"})
    @CacheEvict(value = "bill", allEntries = true)

    @LogAnnotation(module = "ai", operator = "OCR记账")
    public ResponseResult OCR2(@RequestParam("file") MultipartFile file) throws Exception {
        byte[] imgData = file.getBytes();
        String type = sendRequest("https://api.textin.com/robot/v1.0/api/general_receipt_classify", imgData).getJSONObject("result").getString("type");
        switch (type) {
            case "taxi_ticket":
                return taxi(imgData);
            case "air_transport":
                return air(imgData);
            case "train_ticket":
                return train(imgData);
            default:
                return shop(imgData);
        }
    }

    public ResponseResult shop(byte[] bytes) throws IOException {
        JSONArray itemListArray = sendRequest("https://api.textin.com/robot/v1.0/api/receipt", bytes).getJSONObject("result").getJSONArray("item_list");
        HeHeJSON money =  itemListArray.getJSONObject(0).toJavaObject(HeHeJSON.class);
        HeHeJSON date = itemListArray.getJSONObject(1).toJavaObject( HeHeJSON.class);
        HeHeJSON shop =  itemListArray.getJSONObject(3).toJavaObject(HeHeJSON.class);
        HeHeJSON items = itemListArray.getJSONObject(5).toJavaObject( HeHeJSON.class);
        Bill bill = new Bill();
        if (date.getValue().length() >= 8) {
            bill.setCreateTime(date.getValue());
        } else {
            bill.setCreateTime(DateUtil.now());
        }
        String pd = pd(bytes);
        if (!pd.equals("no")) {
            shop.setValue(pd);
        }
        bill.setNum(Double.valueOf(Double.parseDouble(money.getValue())));
        bill.setTypeOne(1);
        bill.setTypeTwo("购物消费");
        bill.setText("于" + bill.getCreateTime() + "在" + shop.getValue() + "购买了" + items.getValue() + "。花费" + money.getValue() + "元");
        bill.setTypeThree(3);
        return ResponseResult.okResult(bill);
    }

    public ResponseResult train(byte[] bytes) throws IOException {
        JSONArray itemListArray = sendRequest("https://api.textin.com/robot/v1.0/api/train_ticket", bytes).getJSONObject("result").getJSONArray("item_list");
        HeHeTrain begin = itemListArray.getJSONObject(2).toJavaObject(HeHeTrain.class);
        HeHeTrain trainNum = itemListArray.getJSONObject(3).toJavaObject(HeHeTrain.class);
        HeHeTrain end =  itemListArray.getJSONObject(4).toJavaObject(HeHeTrain.class);
        HeHeTrain time =  itemListArray.getJSONObject(5).toJavaObject( HeHeTrain.class);
        HeHeTrain price = itemListArray.getJSONObject(7).toJavaObject(HeHeTrain.class);
        Bill bill = new Bill();
        if (time.getValue().length() >= 8) {
            bill.setCreateTime(time.getValue());
        } else {
            bill.setCreateTime(DateUtil.now());
        }
        bill.setNum(Double.valueOf(Double.parseDouble(price.getValue())));
        bill.setTypeOne(1);
        bill.setTypeTwo("出行交通");
        bill.setText("于" + bill.getCreateTime() + "乘坐班次" + trainNum.getValue() + "列车，从" + begin.getValue() + "至" + end.getValue() + "花费" + price.getValue() + "元");
        bill.setTypeThree(3);
        return ResponseResult.okResult(bill);
    }

    public ResponseResult taxi(byte[] bytes) throws IOException {
        String time;
        JSONArray itemListArray = sendRequest("https://api.textin.com/robot/v1.0/api/taxi_invoice", bytes).getJSONObject("result").getJSONArray("item_list");
        HeHeTaxi d = (HeHeTaxi) itemListArray.getJSONObject(2).toJavaObject( HeHeTaxi.class);
        String str2 = ((HeHeTaxi) itemListArray.getJSONObject(4).toJavaObject(HeHeTaxi.class)).getValue();
        String str = d.getValue();
        SimpleDateFormat formatter = new SimpleDateFormat(DatePattern.CHINESE_DATE_PATTERN);
        HeHeTaxi price = (HeHeTaxi) itemListArray.getJSONObject(7).toJavaObject(HeHeTaxi.class);
        HeHeTaxi mile = (HeHeTaxi) itemListArray.getJSONObject(6).toJavaObject(HeHeTaxi.class);
        try {
            try {
                Date date = formatter.parse(str);
                formatter.applyPattern("yyyy-MM-dd");
                String str3 = formatter.format(date) + " " + str2;
                time = DateUtil.now();
            } catch (Exception e) {
                e.printStackTrace();
                time = DateUtil.now();
            }
            Bill bill = new Bill();
            bill.setCreateTime(time);
            bill.setNum(Double.valueOf(Double.parseDouble(price.getValue().replace("元", ""))));
            bill.setTypeOne(1);
            bill.setTypeTwo("出行交通");
            bill.setText("于" + bill.getCreateTime() + "乘坐出租车，行驶" + mile.getValue() + "花费" + price.getValue());
            bill.setTypeThree(3);
            return ResponseResult.okResult(bill);
        } catch (Throwable th) {
            DateUtil.now();
            throw th;
        }
    }

    public ResponseResult air(byte[] bytes) throws IOException {
        JSONObject jsonObject = sendRequest("https://api.textin.com/robot/v1.0/api/air_transport_itinerary", bytes);
        JSONArray itemListArray = jsonObject.getJSONObject("result").getJSONArray("item_list");
        JSONArray flightListArray = jsonObject.getJSONObject("result").getJSONArray("flight_data_list").getJSONArray(0);
        HeHeTaxi price = itemListArray.getJSONObject(14).toJavaObject( HeHeTaxi.class);
        String time = (flightListArray.getJSONObject(5).toJavaObject( HeHeTaxi.class)).getValue() + " " + ((flightListArray.getJSONObject(6).toJavaObject( HeHeTaxi.class)).getValue());
        HeHeTaxi begin = flightListArray.getJSONObject(0).toJavaObject( HeHeTaxi.class);
        HeHeTaxi end =  flightListArray.getJSONObject(1).toJavaObject( HeHeTaxi.class);
        String text = "于" + time + "乘坐" + (flightListArray.getJSONObject(2).toJavaObject(HeHeTaxi.class)).getValue()  + flightListArray.getJSONObject(3).toJavaObject(HeHeTaxi.class).getValue() + ")从" + begin.getValue() + "到" + end.getValue() + "。票价：" + price.getValue() + "元";
        Bill bill = new Bill();
        bill.setCreateTime(time);
        bill.setNum(Double.valueOf(Double.parseDouble(price.getValue().replace("元", ""))));
        bill.setTypeOne(1);
        bill.setTypeTwo("出行交通");
        bill.setTypeThree(3);
        bill.setText(text);
        return ResponseResult.okResult(bill);
    }

    public String pd(byte[] bytes) throws IOException {
        String result = "no";
        String str = sendRequest("https://api.textin.com/robot/v1.0/api/text_recognize_3d1", bytes).getJSONObject("result").getString("whole_text");
        if (str.contains("饿了")) {
            result = "饿了么";
        } else if (str.contains("美团")) {
            result = "美团外卖";
        } else if (str.contains("京东")) {
            result = "京东到家";
        }
        return result;
    }


}
