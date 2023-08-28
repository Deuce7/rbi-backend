package com.rzg.bizmq;

import com.rabbitmq.client.Channel;
import com.rzg.common.ErrorCode;
import com.rzg.constant.BIMqConstant;
import com.rzg.exception.BusinessException;
import com.rzg.manager.AIManager;
import com.rzg.model.entity.Chart;
import com.rzg.model.enums.ChartStatusEnum;
import com.rzg.service.ChartService;
import com.rzg.utils.ExcelUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class BIMessageConsumer {

    @Resource
    private ChartService chartService;

    @Resource
    private AIManager aiManager;

    // 指定程序监听的消息队列确认机制
    @SneakyThrows
    @RabbitListener(queues = {BIMqConstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag){
        if(StringUtils.isBlank(message)){
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
        }
        long chartId = Long.parseLong(message);
        Chart chart = chartService.getById(chartId);

        if(chart == null){
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "图表为空");
        }
        // 修改图表任务状态为执行中
        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setStatus(ChartStatusEnum.RUNNING.getValue());
        boolean b = chartService.updateById(updateChart);
        if(!b){
            channel.basicNack(deliveryTag, false, false);
            handleUpdateChartError(chart.getId(), "更新图表执行中状态失败");
            return;
        }
        // 调用 AI
        String result = aiManager.doChat(BIMqConstant.BI_MODEL_ID, userInput.toString());
        String[] splits = result.split("【【【【【\n");
        if(splits.length < 3){
            channel.basicNack(deliveryTag, false, false);
            handleUpdateChartError(chart.getId(), "AI 生成错误！");
            return;
        }
        String genChart = splits[1];
        String genResult = splits[2];
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chart.getId());
        updateChartResult.setStatus(ChartStatusEnum.SUCCEED.getValue());
        updateChartResult.setGenChart(genChart);
        updateChartResult.setGenResult(genResult);
        boolean updateResult = chartService.updateById(updateChartResult);
        if(!updateResult){
            channel.basicNack(deliveryTag, false, false);
            handleUpdateChartError(chart.getId(), "更新图表成功状态失败");
        }

        // 消息确认
        channel.basicAck(deliveryTag, false);
    }

    private String buildUserInput(Chart chart){
        String goal = chart.getGoal();
        String chartType = chart.getChartType();
        String csvData = chart.getChartData();

        // 拼接分析目标
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        userInput.append(csvData);
        return userInput.toString();
    }

    private void handleUpdateChartError(long chartId, String execMessage){
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus(ChartStatusEnum.FAILED.getValue());
        updateChartResult.setExecMessage(execMessage);
        boolean updateResult = chartService.updateById(updateChartResult);
        if(!updateResult){
            log.error("更新图表失败状态失败" + chartId + "，" + execMessage);
        }
    }
}
