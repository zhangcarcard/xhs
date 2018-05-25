package com.mofan.xhs.websocket;

import com.alibaba.fastjson.JSONObject;
import com.mofan.xhs.domain.UserDO;
import com.mofan.xhs.service.UserService;
import com.mofan.xhs.service.XhsService;
import com.mofan.xhs.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@ServerEndpoint("/websocket")
@Component
public class WebSocket {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocket.class);

    private ExecutorService executor = Executors.newFixedThreadPool(1);
    private Session session;

    private UserService userService = SpringContextUtils.getBean(UserService.class);
    private XhsService xhsService = SpringContextUtils.getBean(XhsService.class);

    /**
     * 连接成功.
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }
    
    /**
     * 断开连接.
     */
    @OnClose
    public void onClose() {
        LOGGER.info("websocket已经关闭.");
    }

    /**
     * 收到消息.
     *
     * @param message
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message) {
        List<UserDO> users = userService.selectAll();
        if (users.size() == 0) {
            JSONObject ret = new JSONObject();
            ret.put("tag", "operate");
            ret.put("message", "当前系统中没有用户.");
            this.sendMessage(ret.toJSONString(), session);
            return;
        } else {
            JSONObject ret = new JSONObject();
            ret.put("tag", "operate");
            ret.put("message", "系统正在运行.");
            this.sendMessage(ret.toJSONString(), session);
        }

    	String[] params = message.split("&");
        JSONObject json = new JSONObject();
        List<String> operates = new ArrayList<>();

        for (String param : params) {
            String[] kv = param.split("=");
            String k = kv[0];
            if ("operate".equalsIgnoreCase(k)) {
                operates.add(kv[1]);
                continue;
            }
            json.put(kv[0], kv.length == 2 ? kv[1] : "");
        }

        LOGGER.info(json.toJSONString());
        JSONObject ret = new JSONObject();
        int follows = 0;
        int likes = 0;
        int collects = 0;
        int cts = 0;
        String comments = null;
        if (operates.contains("follow")) {
            follows = json.getIntValue("follows");
        }
        if (operates.contains("like")) {
            likes = json.getIntValue("likes");
        }
        if (operates.contains("collect")) {
            collects = json.getIntValue("collects");
        }
        if (operates.contains("comment")) {
            comments = json.getString("comments");
            cts = json.getIntValue("cts");
        }

        int seq = 1;
        int index = 0;
        while (follows > 0 || likes > 0 || collects > 0 || cts > 0) {
            if (index == users.size())
                break;

            UserDO user = users.get(index++);
            ret.put("id", seq++);
            ret.put("tag", "table");
            ret.put("username", user.getUsername());
            ret.put("sid", user.getSid());

            if (follows-- > 0) {
                Future<Boolean> future = executor.submit(() -> {
                    try {
                        return xhsService.follow(json.getString("userId"), user.getDeviceId(), user.getSid());
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                    }
                    return false;
                });
                //'id', 'username', 'sid', 'follow', 'like', 'comment', 'collect'
                try {
                    boolean r = future.get();
                    ret.put("follow", r ? "关注成功" : "关注失败");
                } catch (Exception e) {
                    ret.put("follow", "关注失败");
                }
            }
            if (likes-- > 0) {
                Future<Boolean> future = executor.submit(() -> {
                    try {
                        return xhsService.like(json.getString("noteId"), user.getDeviceId(), user.getSid());
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                    }
                    return false;
                });
                try {
                    boolean r = future.get();
                    ret.put("like", r ? "点赞成功" : "点赞失败");
                } catch (Exception e) {
                    ret.put("like", "点赞失败");
                }
            }
            if (collects-- > 0) {
                Future<Boolean> future = executor.submit(() -> {
                    try {
                        return xhsService.collect(json.getString("noteId"), user.getBoardId(), user.getDeviceId(), user.getSid());
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                    }
                    return false;
                });
                try {
                    boolean r = future.get();
                    ret.put("collect", r ? "收藏成功" : "收藏失败");
                } catch (Exception e) {
                    ret.put("collect", "收藏失败");
                }
            }

            final String content = comments;
            if (cts-- > 0) {
                Future<Boolean> future = executor.submit(() -> {
                    try {
                        return xhsService.comment(content, json.getString("noteId"), user.getDeviceId(), user.getSid());
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                    }
                    return false;
                });
                try {
                    boolean r = future.get();
                    ret.put("comment", r ? "评论成功" : "评论失败");
                } catch (Exception e) {
                    ret.put("comment", "评论失败");
                }
            }

            this.sendMessage(ret.toJSONString(), session);
            LOGGER.info(ret.toJSONString());
        }
    }
    
    @OnError
    public void onError(Session session, Throwable t) {
    	LOGGER.error("websocket出错!", t);
    }

    /**
     * 发送消息
     *
     * @param message
     * @throws IOException
     */
    private void sendMessage(String message, Session session) {
    	try {
            session.getBasicRemote().sendText(message);
    	} catch (IOException e) {
    		LOGGER.error("发送websocket消息失败!", e);
		}
    }
}
