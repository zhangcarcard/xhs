package com.mofan.xhs.controller;

import com.alibaba.fastjson.JSON;
import com.mofan.xhs.VO.ResultVO;
import com.mofan.xhs.VO.TableVO;
import com.mofan.xhs.domain.Page;
import com.mofan.xhs.domain.UserDO;
import com.mofan.xhs.service.UserService;
import com.mofan.xhs.service.XhsService;
import com.mofan.xhs.util.PhoneUtils;
import com.mofan.xhs.util.UuidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private XhsService xhsService;

    @RequestMapping("/user/list.html")
    public String html() {
        return "user/list";
    }

    @ResponseBody
    @RequestMapping(value = "/user.do", method = RequestMethod.GET)
    public TableVO page(UserDO userDO, Integer limit, Integer offset) {
        Page<UserDO> page = new Page<>();
        page.setCondition(userDO);
        page.setLimit(limit);
        page.setOffset(offset);
        page = userService.pageBySelective(page);
        return TableVO.successResult(page.getTotal(), page.getResults());
    }

    @ResponseBody
    @RequestMapping(value = "/user.do", method = RequestMethod.POST)
    public ResultVO add(UserDO userDO) {
        Date now = new Date();
        userDO.setImei(PhoneUtils.getIMEI());
        userDO.setDeviceId(UuidUtils.uuid());
        userDO.setGmtCreate(now);
        try {
            String boardId = xhsService.boardLite(userDO.getDeviceId(), userDO.getSid());
            userDO.setBoardId(boardId);
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
        int count = 0;
        try {
            count = userService.insertSelective(userDO);
        } catch (Exception e) {
            if (e.getMessage().contains("UNIQUE constraint"))
                throw new RuntimeException("用户名或者用户密钥不能与已存在的重复!");
            return ResultVO.failResult(e.getMessage());
        }
        return ResultVO.successResult(count);
    }

    @ResponseBody
    @RequestMapping(value = "/user.do", method = RequestMethod.DELETE)
    public ResultVO delete(@RequestBody ArrayList<UserDO> users) {
        int count = userService.deleteByPrimaryKey(users);
        return ResultVO.successResult(count);
    }
}
