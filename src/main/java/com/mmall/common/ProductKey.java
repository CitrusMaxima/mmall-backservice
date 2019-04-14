package com.mmall.common;

public class ProductKey extends BasePrefix {

    public ProductKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static ProductKey getProductStock = new ProductKey(0, "pStock");
    public static ProductKey getProductStatus = new ProductKey(0, "pStatus");
    public static ProductKey getProductName = new ProductKey(0, "pName");
}
