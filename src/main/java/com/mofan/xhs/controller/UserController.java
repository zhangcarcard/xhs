package com.mofan.xhs.controller;

import com.mofan.xhs.domain.Page;
import com.mofan.xhs.domain.UserDO;
import com.mofan.xhs.service.UserService;
import com.mofan.xhs.util.PhoneUtils;
import com.mofan.xhs.util.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/page")
    public Page<UserDO> page(UserDO userDO, Integer limit, Integer offset) {
        Page<UserDO> page = new Page<>();
        page.setCondition(userDO);
        page.setLimit(limit);
        page.setOffset(offset);
        return userService.pageBySelective(page);
    }

    @RequestMapping("/add")
    public int add(UserDO userDO) {
        Date now = new Date();
        userDO.setImei(PhoneUtils.getIMEI());
        userDO.setDeviceId(UuidUtils.uuid());
        userDO.setGmtCreate(now);
        return userService.insertSelective(userDO);
    }
}
