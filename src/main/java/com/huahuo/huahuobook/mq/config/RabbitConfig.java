package com.huahuo.huahuobook.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @作者 花火
 * @创建日期 2023/3/9 17:27
 */
@Configuration
public class RabbitConfig {
    public static final String EXCHANGE_NAME = "log_exchange";
    public static final String QUEUE_NAME = "log_queue";
    @Bean("logExchange")
    public Exchange bootExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean("logQueue")
    public Queue bootQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }
    @Bean
    public Binding bindQueueExchange(@Qualifier("logQueue") Queue queue, @Qualifier("logExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("log.info").noargs();
    }

}
