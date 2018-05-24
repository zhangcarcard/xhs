package com.mofan.xhs.service;

import java.io.IOException;

/**
 * Created by Zhangka in 2018/05/24
 */
public interface XhsService {
    /**
     * 关注.
     * @param userIds
     * @param deviceId
     * @param sid
     * @return
     * @throws IOException
     */
    boolean follow(String userIds, String deviceId, String sid) throws IOException;

    /**
     * 点赞.
     * @param noteId
     * @param deviceId
     * @param sid
     * @return
     * @throws IOException
     */
    boolean like(String noteId, String deviceId, String sid) throws IOException;

    /**
     * 收藏.
     * @param noteId
     * @param boardId
     * @param deviceId
     * @param sid
     * @return
     * @throws IOException
     */
    boolean collect(String noteId, String boardId, String deviceId, String sid) throws IOException;

    /**
     * 评论.
     * @param content
     * @param noteId
     * @param deviceId
     * @param sid
     * @return
     * @throws IOException
     */
    boolean comment(String content, String noteId, String deviceId, String sid) throws IOException;

    /**
     * 收藏夹.
     * @param deviceId
     * @param sid
     * @return
     * @throws IOException
     */
    String boardLite(String deviceId, String sid) throws IOException;
}
