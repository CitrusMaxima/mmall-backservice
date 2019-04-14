package com.mmall.service.impl;

import com.mmall.pojo.Cart;
import com.mmall.pojo.OrderItem;
import com.mmall.config.MQConfig;
import com.mmall.service.IMQReceiverService;
import com.mmall.service.IOrderService;
import com.mmall.util.CommonUtil;
import com.mmall.vo.MQMessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("iMQReceiverService")
@Slf4j
public class MQReceiverServiceImpl implements IMQReceiverService {
    
    @Autowired
    private IOrderService iOrderService;

    @Override
    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message) {
        log.info("receive message:" + message);

        MQMessageVo mqMessageVo = CommonUtil.stringToBean(message, MQMessageVo.class);
        Integer userId = mqMessageVo.getUserId();
        Integer shippingId = mqMessageVo.getShippingId();
        Long orderNo = mqMessageVo.getOrderNo();
        BigDecimal payment = mqMessageVo.getPayment();
        List<Cart> cartList = mqMessageVo.getCartList();
        List<OrderItem> orderItemList = mqMessageVo.getOrderItemList();

        iOrderService.doCreateOrder(userId, shippingId, orderNo, payment, cartList, orderItemList);
    }
}
