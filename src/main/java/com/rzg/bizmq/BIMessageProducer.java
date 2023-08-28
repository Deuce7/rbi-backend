package com.rzg.bizmq;

import com.rabbitmq.client.Channel;
import com.rzg.constant.BIMqConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BIMessageProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     * @param message
     */
    public void sendMessage(String message){
        rabbitTemplate.convertAndSend(BIMqConstant.BI_EXCHANGE_NAME, BIMqConstant.BI_ROUTING_KEY, message);
    }
}
