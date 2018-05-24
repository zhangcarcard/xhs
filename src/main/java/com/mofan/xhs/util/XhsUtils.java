package com.mofan.xhs.util;

import com.mofan.xhs.constant.XhsConsts;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class XhsUtils {
    /**
     * 关注POST.
     * @param userIds
     * @param deviceId
     * @param sid
     * @return
     */
    public static String getFollowQueryString(String userIds, String deviceId, String sid) {
        Map<String, String> params = new HashMap<>(XhsConsts.params);
        params.put("userids", userIds);
        params.put("deviceId", deviceId);
        params.put("sid", sid);
        params.put("t", String.valueOf(XhsConsts.currentSeconds()));

        String queryString = getQueryString(params);
        return queryString + "sign=" + SignUtils.sign(queryString, deviceId);
    }

    /**
     * 点赞GET.
     * @param noteId
     * @param deviceId
     * @param sid
     * @return
     */
    public static String getLikeQueryString(String noteId, String deviceId, String sid) {
        Map<String, String> params = new HashMap<>(XhsConsts.params);
        String oid = "discovery." + noteId;
        params.put("oid", oid);
        params.put("deviceId", deviceId);
        params.put("sid", sid);
        params.put("t", String.valueOf(XhsConsts.currentSeconds()));

        String queryString = getQueryString(params);
        return queryString + "sign=" + SignUtils.sign(queryString, deviceId);
    }

    /**
     * 收藏POST.
     * @param noteId
     * @param boardId
     * @param deviceId
     * @param sid
     * @return
     */
    public static String getCollectQueryString(String noteId, String boardId, String deviceId, String sid) {
        Map<String, String> params = new HashMap<>(XhsConsts.params);
        params.put("note_id", noteId);
        params.put("board_id", boardId);
        params.put("deviceId", deviceId);
        params.put("reason", "");
        params.put("sid", sid);
        params.put("t", String.valueOf(XhsConsts.currentSeconds()));

        String queryString = getQueryString(params);
        return queryString + "sign=" + SignUtils.sign(queryString, deviceId);
    }

    /**
     * 评论POST.
     * @param content
     * @param noteId
     * @param deviceId
     * @param sid
     * @return
     */
    public static String getCommentQueryString(String content, String noteId, String deviceId, String sid) {
        Map<String, String> params = new HashMap<>(XhsConsts.params);
        params.put("content", content);
        params.put("at_users", "[]");
        params.put("hash_tags", "[]");
        params.put("noteid", noteId);
        params.put("deviceId", deviceId);
        params.put("sid", sid);
        params.put("t", String.valueOf(XhsConsts.currentSeconds()));

        String queryString = getQueryString(params);
        return queryString + "sign=" + SignUtils.sign(queryString, deviceId);
    }

    /**
     * 收藏夹GET.
     * @param deviceId
     * @param sid
     * @return
     */
    public static String getBoardLiteQueryString(String deviceId, String sid) {
        Map<String, String> params = new HashMap<>(XhsConsts.params);
        params.put("page", "1");
        params.put("deviceId", deviceId);
        params.put("sid", sid);
        params.put("t", String.valueOf(XhsConsts.currentSeconds()));

        String queryString = getQueryString(params);
        return queryString + "sign=" + SignUtils.sign(queryString, deviceId);
    }

    public static String getQueryString(Map<String, String> params) {
        StringBuilder builder = new StringBuilder(512);
        params.forEach((k, v) -> builder.append(k).append("=").append(v).append("&"));
        return builder.toString();
    }
}
