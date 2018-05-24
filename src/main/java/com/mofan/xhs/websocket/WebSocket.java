package com.mofan.xhs.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/websocket")
@Component
public class WebSocket {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocket.class);

    private Session session;
    /**
     * 连接成功.
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        LOGGER.info(this.toString());
        LOGGER.info("websocket已经打开.");
        this.sendMessage("websocket已经连接.", session);
        for (int i = 0; i < 10; ++i) {
            this.sendMessage(System.currentTimeMillis()+"", session);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
    	LOGGER.info("websocket接收到消息:" + message);
        this.sendMessage(System.currentTimeMillis()+"", session);
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
