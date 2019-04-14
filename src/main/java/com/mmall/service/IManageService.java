package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.ManageCountVo;

public interface IManageService {
    ServerResponse<ManageCountVo> getBaseCount();
}
