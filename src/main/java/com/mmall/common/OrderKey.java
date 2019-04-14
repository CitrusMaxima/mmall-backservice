package com.mmall.common;

public class OrderKey extends BasePrefix {

    public OrderKey(String prefix) {
        super(0, prefix);
    }

    public static OrderKey getOrderVo = new OrderKey("gov");
}
