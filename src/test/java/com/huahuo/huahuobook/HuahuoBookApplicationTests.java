package com.huahuo.huahuobook;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huahuo.huahuobook.common.ResponseResult;
import com.huahuo.huahuobook.common.aop.LogAnnotation;
import com.huahuo.huahuobook.dto.HeHeJSON;
import com.huahuo.huahuobook.dto.HeHeTaxi;
import com.huahuo.huahuobook.dto.HeHeTrain;
import com.huahuo.huahuobook.pojo.Bill;
import com.huahuo.huahuobook.pojo.Book;
import com.huahuo.huahuobook.service.BillService;
import com.huahuo.huahuobook.service.BookService;
import com.huahuo.huahuobook.utils.FileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.apache.commons.lang3.RandomUtils.nextDouble;
import static org.apache.commons.lang3.RandomUtils.nextInt;

@SpringBootTest
public class HuahuoBookApplicationTests {
    @Autowired
    BillService billService;

    private static final String appId = "c23faa38869314f56d48d6615185d9f8";
    //c23faa38869314f56d48d6615185d9f8
    private static final String secretCode = "87aa131bfefa97b15b3a83eb9bd4a07a";
    //87aa131bfefa97b15b3a83eb9bd4a07a
    @Test   //生成数据
    public void createData() {
        int num = 1000;  //输入要生成数据条数
        Bill bill = new Bill();
        ArrayList<Bill> bills = new ArrayList<>();
        while (num != 0) {
            bill.setBookId(1);
            bill.setText("这里是我的备注噢");
            num--;
            Random random = new Random();
            int minDay = (int) LocalDate.of(2017, 6, 1).toEpochDay();
            int maxDay = (int) LocalDate.of(2023, 6, 1).toEpochDay();
            long randomDay = minDay + random.nextInt(maxDay - minDay);
            LocalDate randomBirthDate = LocalDate.ofEpochDay(randomDay);
            String date = randomBirthDate.toString();
            Integer num2 = nextInt(0,50);
            Double num3 = num2.doubleValue();
            int type = nextInt(1, 3);
            int type2 = nextInt(0, 8);
            int type6 = nextInt(0, 8);
            int type5 = nextInt(1, 5);
            bill.setPayWay(type5);
            String type3[] = new String[]{"食品餐饮", "出行交通", "休闲娱乐", "购物消费", "文化教育", "健康医疗", "酒店住宿", "居家生活"};
            String addType[] = new String[]{"中奖","理财产品","借款","奖金绩效","工资","闲置售卖","公账报销","其他"};
            String type4 = type3[type2];
            bill.setTypeOne(type);
            bill.setNum(num3);
            bill.setCreateTime(date);
            bill.setTypeTwo(type4);
            if (bill.getTypeOne() == 2)
                bill.setTypeTwo(addType[type6]);
            bills.add(bill);
            billService.add(bill);
        }


    }
    @Autowired
    BookService bookService;


    @Test
    void deleteBookID(){
        LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bill::getBookId,1);
        billService.remove(queryWrapper);
    }
    @Test   //更新账本总收入 总支出
    void sort() {
        List<Book> list = bookService.list();
        LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<>();
        for (Book book : list) {
            Integer bookId = book.getId();
            queryWrapper.eq(Bill::getBookId,bookId);
            List<Bill> list1 = billService.list(queryWrapper);
            for (Bill bill : list1) {
                if(bill.getTypeOne()==1)
                    book.setTotalExpense(book.getTotalExpense()+bill.getNum());
                else {
                    book.setTotalIncome(book.getTotalIncome()+bill.getNum());
                }
            }
        }
       bookService.updateBatchById(list);
    }



    public JSONObject sendRequest(String url, byte[] imgData) throws IOException, ProtocolException {
        System.out.println("send request......");
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
                System.out.println("send! success!");
                return JSONObject.parseObject(result);
            }
            result = result + line;
        }
    }

    @PostMapping({"/ocr/v2"})
    @LogAnnotation(module = "ai", operator = "OCR记账")
    public void OCR2(File file) throws Exception {
        System.out.println("============================");
        byte[] imgData = FileUtil.fileToByte(file);
        String type = sendRequest("https://api.textin.com/robot/v1.0/api/general_receipt_classify", imgData).getJSONObject("result").getString("type");
        System.out.println("begin analyze......");
        switch (type) {
            case "taxi_ticket":
           taxi(imgData);
            case "air_transport":
           air(imgData);
            case "train_ticket":
               train(imgData);
            default:
               shop(imgData);
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
        bill.setBookId(50);
        bill.setUserId(211);
        billService.save(bill);
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
        bill.setBookId(50);
        bill.setUserId(211);
        billService.save(bill);
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
            bill.setBookId(50);
            bill.setUserId(211);
            billService.save(bill);
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
        bill.setBookId(50);
        bill.setUserId(211);
        billService.save(bill);
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
@Test
    void hehedatas() throws Exception {

int success = 0;
        for (int i = 1300 ;i <=1499 ; i++) {   //1499
            System.out.println("===================================="+i+"================================");
            String name = i+".jpg";
            String path = "C:/output (2)/output/"+name;
            File file = new File(path);
 try{
     System.out.println("开始OCR");
     OCR2(file);}
 catch (Exception e){
     continue;
 }
success++;
            System.out.println("==============="+i+"========================================="+"OK!!");

            System.out.println("===================success===>>>"+success+"==============total===>"+i);

        }

    }
}