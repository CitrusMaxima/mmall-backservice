package com.mmall.service.impl;

import com.mmall.config.MQConfig;
import com.mmall.service.IMQSenderService;
import com.mmall.util.CommonUtil;
import com.mmall.vo.MQMessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iMQSenderService")
@Slf4j
public class MQSenderServiceImpl implements IMQSenderService {

    @Autowired
    AmqpTemplate amqpTemplate;

    @Override
    public void sendMQMessage(MQMessageVo mqMessageVo) {
        String msg = CommonUtil.beanToString(mqMessageVo);
        log.info("send message:" + msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
    }
}
