package com.huahuo.huahuobook.service;

import com.huahuo.huahuobook.common.ResponseResult;
import com.huahuo.huahuobook.dto.*;
import com.huahuo.huahuobook.pojo.Book;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Administrator
 * @description 针对表【book】的数据库操作Service
 * @createDate 2023-01-27 17:35:12
 */
public interface BookService extends IService<Book> {
    public ResponseResult<String> createNewBook(@RequestBody BookDto bookDto);
    public Integer createNewBook1(@RequestBody BookDto bookDto);
    public ResponseResult listBooks(ListBookDto dto);

    public ResponseResult createTempBook(BookDto dto);

    public ResponseResult addTempBook(TempBookDto dto);
    public ResponseResult updateBook(@RequestBody BookDto dto);
    public ResponseResult<String> deleteBook(Integer id);
    public ResponseResult updateBookBudget(@RequestBody BudgetDto dto);
    public void createExcel(@PathVariable Integer id, HttpServletResponse response) throws IOException;
}
