package com.huahuo.huahuobook.mq.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huahuo.huahuobook.mq.config.RabbitConfig;
import com.huahuo.huahuobook.pojo.LogInfo;
import com.huahuo.huahuobook.pojo.User;
import com.huahuo.huahuobook.service.LogInfoService;
import com.huahuo.huahuobook.service.UserService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @作者 花火
 * @创建日期 2023/3/6 0:06
 */
@Component
public class RabbitMQListener {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserService userService;
    @Autowired
    LogInfoService logInfoService;

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void ListenerQueue2(Message message) {
        try {
            byte[] bytes = message.getBody();
            LogInfo logInfo = objectMapper.readValue(bytes, LogInfo.class);
            if (logInfo.getUserId() != null) {
                User byId = userService.getById(logInfo.getUserId());
                String username = byId.getNickName();
                logInfo.setUsername(username);
            }
            logInfoService.save(logInfo);
        } catch (Exception e) {
            System.out.println("发生错误");
        }
    }
}