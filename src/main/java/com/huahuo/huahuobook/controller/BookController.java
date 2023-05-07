package com.huahuo.huahuobook.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huahuo.huahuobook.common.ResponseResult;
import com.huahuo.huahuobook.common.aop.LogAnnotation;
import com.huahuo.huahuobook.dto.*;

import com.huahuo.huahuobook.pojo.Book;
import com.huahuo.huahuobook.pojo.Relation;
import com.huahuo.huahuobook.pojo.User;
import com.huahuo.huahuobook.service.BookService;
import com.huahuo.huahuobook.service.FriendService;

import com.huahuo.huahuobook.service.RelationService;
import com.huahuo.huahuobook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @作者 花火
 * @创建日期 2023/1/27 17:10
 */
@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private FriendService friendService;
@Autowired
private  RelationService relationService;
@Autowired
private UserService userService;
    /**
     * 创建新账本
     * @param bookDto
     * @return
     */
    @PostMapping("/create")
    @LogAnnotation(module="book",operator="新建账本")
    public ResponseResult<String> createNewBook(@RequestBody BookDto bookDto) {
        return bookService.createNewBook(bookDto);
    }

    /**
     * 按照类型查看账本
     * 1 普通账本
     * 2 共享账本
     * 3 临时账本（活动账本）
     * @param dto
     * @return
     */
    @PostMapping("/list")
    @LogAnnotation(module="book",operator="查询账本列表")
    public ResponseResult listBooks(@RequestBody ListBookDto dto) {
        return bookService.listBooks(dto);
    }

    /**
     *  分享账本
     * @param dto
     * @return
     */
    @PostMapping("/share/1")
    @LogAnnotation(module="book",operator="共享账本")
    public ResponseResult<String> shareBookWithFriend(@RequestBody ShareBookDto dto) {

        return friendService.shareBookWithFriend(dto);
    }

    //取消共享
    @PostMapping("/share/0")
    @LogAnnotation(module="book",operator="取消共享账本")
    public ResponseResult<String> cancelShareBookWithFriend(@RequestBody ShareBookDto dto) {

        return friendService.cancelShareBookWithFriend(dto);
    }

    //创建一个临时账本
    @PostMapping("/create/temp")
    @LogAnnotation(module="book",operator="创建临时账本")
    public ResponseResult createTempBook(@RequestBody BookDto bookDto) {
        return bookService.createTempBook(bookDto);
    }

    //通过临时码加入账本
    @PostMapping("/add/temp")
    @LogAnnotation(module="book",operator="通过临时码加入账本")
    public ResponseResult addTempBook(@RequestBody TempBookDto dto) {
        return bookService.addTempBook(dto);
    }

//删除账本
    @GetMapping("/delete/{id}")
    @LogAnnotation(module="book",operator="删除账本")
    public ResponseResult<String> deleteBook(@PathVariable Integer id) {
        return bookService.deleteBook(id);
    }

//导出账本excel
    @GetMapping("/create/excel/{id}")
    @LogAnnotation(module="book",operator="导出excel")
    public void createExcel(@PathVariable Integer id, HttpServletResponse response) throws IOException {
          bookService.createExcel(id,response);
    }

    @PostMapping("/update")
    @LogAnnotation(module="book",operator="修改账本")
    public ResponseResult updateBook(@RequestBody BookDto dto){
       return bookService.updateBook(dto);

    }

    @PostMapping("/update/budget")
    @LogAnnotation(module = "book",operator = "修改预算分布")
    public ResponseResult updateBookBudget(@RequestBody BudgetDto dto)
    {
        return bookService.updateBookBudget(dto);
    }

@GetMapping("/get/user/temp/{id}")
public ResponseResult getTemp(@PathVariable Integer id)
{
    LambdaQueryWrapper<Relation> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Relation::getBookId,id);
    List<Relation> list = relationService.list(queryWrapper);
    System.out.println(list);

    List<Integer> results = new ArrayList<>();
    for (Relation relation : list) {
            results.add(relation.getUserId());
    }
    LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.in(User::getId,results);
    List<User> list1 = userService.list(lambdaQueryWrapper);
    ArrayList<UserBaseInfo> infos = new ArrayList<>();
    for (User user : list1) {
        UserBaseInfo userBaseInfo = new UserBaseInfo();
         userBaseInfo.setUserId(user.getId());
         userBaseInfo.setAgeType(user.getAgeType());
         userBaseInfo.setNickName(user.getNickName());
         userBaseInfo.setHeadImg(user.getHeadImg());
         userBaseInfo.setDefaultBookId(user.getDefaultBookId());
         infos.add(userBaseInfo);
    }

    return ResponseResult.okResult(infos);
}



}
