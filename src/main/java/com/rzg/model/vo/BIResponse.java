package com.rzg.model.vo;

import lombok.Data;

/**
 * BI 的返回结果
 */
@Data
public class BIResponse {

    /**
     *
     */
    private Long chartId;

    /**
     * 生成图表
     */
    private String genChart;

    /**
     * 生成结果
     */
    private String genResult;
}
