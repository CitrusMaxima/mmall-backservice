package com.mmall.controller.backend;

import com.mmall.common.ServerResponse;
import com.mmall.service.IManageService;
import com.mmall.vo.ManageCountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/manage/statistic")
public class ManageController {

    @Autowired
    IManageService iManageService;

    @RequestMapping(value = "/base_count.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ManageCountVo> baseCount() {
        return iManageService.getBaseCount();
    }

}
