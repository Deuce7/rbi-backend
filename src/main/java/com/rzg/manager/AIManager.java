package com.rzg.manager;

import com.rzg.common.ErrorCode;
import com.rzg.exception.ThrowUtils;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * AI 调用
 */
@Service
public class AIManager {

    @Resource
    private YuCongMingClient yuCongMingClient;

    public String doChat(long BIModelId, String message){
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(BIModelId);
        devChatRequest.setMessage(message);
        BaseResponse<DevChatResponse> response = yuCongMingClient.doChat(devChatRequest);
        ThrowUtils.throwIf(response == null, ErrorCode.SYSTEM_ERROR, "AI 响应错误！");
        return response.getData().getContent();
    }

}
