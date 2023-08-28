package com.rzg.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rzg.model.dto.chart.ChartQueryRequest;
import com.rzg.model.dto.post.PostQueryRequest;
import com.rzg.model.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rzg.model.entity.Post;

/**
* @author Rzg
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2023-08-16 16:20:07
*/
public interface ChartService extends IService<Chart> {

    QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest);
}
