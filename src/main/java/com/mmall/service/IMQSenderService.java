package com.mmall.service;

import com.mmall.vo.MQMessageVo;

public interface IMQSenderService {
    void sendMQMessage(MQMessageVo mqMessageVo);
}
