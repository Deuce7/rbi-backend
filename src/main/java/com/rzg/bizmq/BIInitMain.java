package com.rzg.bizmq;


import com.rabbitmq.client.*;
import com.rzg.constant.BIMqConstant;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class BIInitMain {
    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            String EXCHANGE_NAME = BIMqConstant.BI_EXCHANGE_NAME;
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            // 创建队列，随机分配一个队列名称
            String queueName = BIMqConstant.BI_QUEUE_NAME;
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, EXCHANGE_NAME, BIMqConstant.BI_ROUTING_KEY);
        } catch (Exception e) {
        }
    }
}