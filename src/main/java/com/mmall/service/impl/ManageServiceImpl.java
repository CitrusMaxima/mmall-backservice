package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.service.IManageService;
import com.mmall.service.IOrderService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.vo.ManageCountVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iManageService")
@Slf4j
public class ManageServiceImpl implements IManageService {

    @Autowired
    IUserService iUserService;

    @Autowired
    IProductService iProductService;

    @Autowired
    IOrderService iOrderService;

    @Override
    public ServerResponse<ManageCountVo> getBaseCount() {
        int userCount = iUserService.getUserCount();
        int productCount = iProductService.getProductCount();
        int orderCount = iOrderService.getOrderCount();

        ManageCountVo manageCountVo = new ManageCountVo();

        manageCountVo.setUserCount(userCount);
        manageCountVo.setProductCount(productCount);
        manageCountVo.setOrderCount(orderCount);

        return ServerResponse.createBySuccess(manageCountVo);
    }
}
