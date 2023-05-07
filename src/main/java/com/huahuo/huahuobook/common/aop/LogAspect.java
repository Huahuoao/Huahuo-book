package com.huahuo.huahuobook.common.aop;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huahuo.huahuobook.pojo.LogInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.function.ServerResponse;

import java.lang.reflect.Method;

/**
 * @作者 花火
 * @创建日期 2023/3/9 16:36
 */
@Component
@Aspect
@Slf4j
public class LogAspect {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    ObjectMapper objectMapper;

    @Pointcut("@annotation(com.huahuo.huahuobook.common.aop.LogAnnotation)")
    public void pt() {
    }


    private void sendToMQ(LogInfo logInfo) {
        try {
            Message message = MessageBuilder.withBody(objectMapper.writeValueAsBytes(logInfo)).build();
            rabbitTemplate.convertAndSend("log_exchange", "log.info", message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Around("pt()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = sra.getRequest().getHeader("token");
        long beginTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long time = System.currentTimeMillis() - beginTime;
        recordLog(joinPoint, time, token);
        return result;


    }

    public void recordLog(ProceedingJoinPoint joinPoint, long time, String token) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Integer userId = null;
        if (StringUtils.isNotBlank(token)) {
            JWT jwt = JWTUtil.parseToken(token);
            userId = (Integer) jwt.getPayload("id");
        }
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        log.info("==================log start==================");
        log.info("调用时间:{}", DateUtil.now());
        log.info("调用者id:{}", userId);
        log.info("调用者token:{}", token);
        log.info("日志级别:{}", "info");
        log.info("所属模块:{}", logAnnotation.module());
        log.info("方法名称:{}", logAnnotation.operator());
        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("请求方法:{}", className + "." + methodName + "()");
        //请求参数
        Object[] args = joinPoint.getArgs();
        String params = null;
        try {
            params = JSON.toJSONString(args[0]);
        } catch (Exception e) {
             params = "file";
        }
        LogInfo logInfo = new LogInfo();
        logInfo.setFunc(className + "." + methodName + "()");
        logInfo.setParams(params);
        logInfo.setCallTime(DateUtil.now());
        logInfo.setModule(logAnnotation.module());
        logInfo.setUserId(userId);
        logInfo.setFunctionName(logAnnotation.operator());
        logInfo.setWasteTime(time);
        sendToMQ(logInfo);
        log.info("参数列表:{}", params);
        log.info("接口耗时:{} ms", time);
        log.info("==================log end==================");

    }


}
