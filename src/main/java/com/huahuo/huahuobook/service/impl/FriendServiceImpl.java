package com.huahuo.huahuobook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huahuo.huahuobook.common.ResponseResult;
import com.huahuo.huahuobook.dto.AddFriendDto;
import com.huahuo.huahuobook.dto.ShareBookDto;
import com.huahuo.huahuobook.mapper.UserMapper;
import com.huahuo.huahuobook.pojo.Friend;
import com.huahuo.huahuobook.pojo.Relation;
import com.huahuo.huahuobook.pojo.User;
import com.huahuo.huahuobook.service.FriendService;
import com.huahuo.huahuobook.mapper.FriendMapper;
import com.huahuo.huahuobook.service.RelationService;
import com.huahuo.huahuobook.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【friend】的数据库操作Service实现
 * @createDate 2023-01-28 15:59:36
 */
@Service
@Slf4j
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend>
        implements FriendService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private RelationService relationService;

    @Override
    public ResponseResult<String> addFriend(AddFriendDto dto) {
        User user = userMapper.selectByPhoneNumber(dto.getUsername());
        log.info("===================================000000");
        Integer id = user.getId();
        Friend friend = new Friend();
        friend.setType(dto.getType());
        friend.setFriendsId(id);
        friend.setUserId(dto.getUserId());
        save(friend);
        log.info("===================================11111");
        Friend friend1 = new Friend();
        friend1.setUserId(id);
        friend1.setType(dto.getType());
        friend1.setFriendsId(dto.getUserId());
        save(friend1);
        log.info("===================================222222");
        return ResponseResult.okResult("添加成功");
    }

    @Override
    public ResponseResult<User> listFriends(Integer id) {
        List<Integer> ids = new ArrayList<>();
        List<Friend> friends = friendMapper.listByUserId(id);
        for (Friend friend : friends) {
            ids.add(friend.getFriendsId());
        }
        List<User> users = userService.listByIds(ids);
        return ResponseResult.okResult(users);
    }

    @Override
    public ResponseResult<String> shareBookWithFriend(ShareBookDto dto) {
        Relation relation = new Relation();
        relation.setBookId(dto.getBookId());
        relation.setUserId(dto.getFriendId());
        relationService.save(relation);
        return ResponseResult.okResult("共享账本成功");
    }

    @Override
    public ResponseResult<String> cancelShareBookWithFriend(ShareBookDto dto) {
        LambdaQueryWrapper<Relation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Relation::getUserId,dto.getFriendId())
                .eq(Relation::getBookId,dto.getBookId());
        relationService.remove(queryWrapper);
        return ResponseResult.okResult("取消共享成功");
    }
}




