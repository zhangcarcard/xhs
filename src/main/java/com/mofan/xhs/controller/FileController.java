package com.mofan.xhs.controller;

import com.mofan.xhs.VO.ResultVO;
import com.mofan.xhs.domain.UserDO;
import com.mofan.xhs.service.UserService;
import com.mofan.xhs.service.XhsService;
import com.mofan.xhs.util.PhoneUtils;
import com.mofan.xhs.util.UuidUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Controller
@RequestMapping("/file")
public class FileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private XhsService xhsService;

    /**
     * 下载模板.
     * @param type
     * @param resp
     */
    @RequestMapping(value = "/template/{type}.do", method = RequestMethod.GET)
    public void template(@PathVariable String type, HttpServletResponse resp) {
        try {
            switch (type) {
                case "user":
                    String title = "#username,sid\nzhangsan,session.1211638814557814674";
                    writeTemplate("users.txt", title.getBytes("UTF-8"), resp);
                    break;
                default:
                    throw new RuntimeException("无法下载模板.");
            }
        } catch (Exception e) {
            throw new RuntimeException("无法下载模板.");
        }
    }

    /**
     * 上传模板.
     * @param type
     * @param p
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/template/{type}.do", method = RequestMethod.POST)
    public ResultVO template(@PathVariable String type, MultipartFile p) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            List<String> lines = reader.lines().map(String::trim).filter(e -> !e.contains("#") && StringUtils.isNotBlank(e)).collect(Collectors.toList());
            Integer count = save(type, lines);

            return count != null && count > 0 ? ResultVO.successResult() : ResultVO.failResult("上传失败.");
        } catch (IOException e) {
            throw new RuntimeException("无法导入文件.");
        }
    }

    /**
     * 写输出流.
     * @param filename
     * @param bytes
     * @param resp
     */
    protected void writeTemplate(String filename, byte[] bytes, HttpServletResponse resp) {
        try (ServletOutputStream out = resp.getOutputStream()) {
            resp.setContentType("application/octet-stream");
            resp.setCharacterEncoding("UTF-8");
            int length = bytes.length;
            resp.setContentLength(length);
            resp.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));

            out.write(bytes, 0, length);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("无法下载模板.");
        }
    }

    public Integer save(String type, List<String> lines) {
        Integer count = 0;
        switch (type) {
            case "user":
                List<UserDO> users = new ArrayList<>();
                lines.forEach(e -> {
                     // username,sid
                    String[] array = e.split(",");
                    if (array.length == 2) {
                        UserDO user = new UserDO();
                        user.setUsername(array[0]);
                        user.setSid(array[1]);
                        user.setDeviceId(UuidUtils.uuid());
                        user.setImei(PhoneUtils.getIMEI());
                        user.setGmtCreate(new Date());
                        try {
                            user.setBoardId(xhsService.boardLite(user.getDeviceId(), user.getSid()));
                            users.add(user);
                        } catch (IOException e1) {
                            LOGGER.error("获取专辑ID出错", e);
                        }
                    }
                });
                if (users.size() > 0) {
                    count = userService.insert(users);
                }
                break;
            default:throw new RuntimeException("类型错误.");
        }

        return count;
    }
}
