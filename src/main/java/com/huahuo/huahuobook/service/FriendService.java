package com.huahuo.huahuobook.service;

import com.huahuo.huahuobook.common.ResponseResult;
import com.huahuo.huahuobook.dto.AddFriendDto;
import com.huahuo.huahuobook.dto.ShareBookDto;
import com.huahuo.huahuobook.pojo.Friend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huahuo.huahuobook.pojo.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
* @author Administrator
* @description 针对表【friend】的数据库操作Service
* @createDate 2023-01-28 15:59:36
*/
public interface FriendService extends IService<Friend> {
    public ResponseResult<String> addFriend(@RequestBody AddFriendDto dto);
    public ResponseResult<User> listFriends(@PathVariable Integer id);
    public ResponseResult<String> shareBookWithFriend(ShareBookDto dto);
    public ResponseResult<String> cancelShareBookWithFriend(@RequestBody  ShareBookDto dto);
}
