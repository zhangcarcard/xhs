package com.mofan.xhs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mofan.xhs.constant.XhsConsts;
import com.mofan.xhs.service.XhsService;
import com.mofan.xhs.util.XhsUtils;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zhangka in 2018/05/24
 */
@Service
public class XhsServiceImpl implements XhsService {
    private static  final Logger LOGGER = LoggerFactory.getLogger(XhsServiceImpl.class);
    public static final OkHttpClient CLIENT = new OkHttpClient().newBuilder().connectTimeout(5L,  TimeUnit.SECONDS).build();

    /**
     * 关注.
     * @param userIds
     * @param deviceId
     * @param sid
     * @return
     * @throws IOException
     */
    @Override
    public boolean follow(String userIds, String deviceId, String sid) throws IOException {
        LOGGER.info("正在关注:userIds=" + userIds + "|deviceId=" + deviceId + "|sid=" + sid);

        String queryString = XhsUtils.getFollowQueryString(userIds, deviceId, sid);
        RequestBody requestBody = RequestBody.create(MediaType.parse(XhsConsts.HTTP_MEDIA_TYPE), queryString);
        Request.Builder builder = new Request.Builder();
        Request request = builder.addHeader("Authorization", sid)
                .addHeader("User-Agent", XhsConsts.HTTP_HEADER_USER_AGENT)
                .addHeader("Host", XhsConsts.HTTP_HEADER_HOST)
                .url(XhsConsts.HOSTNAME + XhsConsts.URL_FOLLOW)
                .post(requestBody)
                .build();

        Response response = CLIENT.newCall(request).execute();
        String content = response.body().string();
        LOGGER.info("关注返回:" + content);
        JSONObject json = JSON.parseObject(content);
        if (json.getIntValue("result") == 0) {
            return true;
        } else {
            throw new RuntimeException(content);
        }
    }

    /**
     * 点赞.
     * @param noteId
     * @param deviceId
     * @param sid
     * @return
     * @throws IOException
     */
    @Override
    public boolean like(String noteId, String deviceId, String sid) throws IOException {
        LOGGER.info("正在点赞:noteId=" + noteId + "|deviceId=" + deviceId + "|sid=" + sid);

        String queryString = XhsUtils.getLikeQueryString(noteId, deviceId, sid);
        Request.Builder builder = new Request.Builder();
        Request request = builder.addHeader("Authorization", sid)
                .addHeader("User-Agent", XhsConsts.HTTP_HEADER_USER_AGENT)
                .addHeader("Host", XhsConsts.HTTP_HEADER_HOST)
                .url(XhsConsts.HOSTNAME + XhsConsts.URL_LIKE + "?" + queryString)
                .get()
                .build();

        Response response = CLIENT.newCall(request).execute();
        String content = response.body().string();
        LOGGER.info("点赞返回:" + content);
        JSONObject json = JSON.parseObject(content);
        if (json.getIntValue("result") == 0) {
            return true;
        } else {
            throw new RuntimeException(content);
        }
    }

    /**
     * 收藏.
     * @param noteId
     * @param boardId
     * @param deviceId
     * @param sid
     * @return
     * @throws IOException
     */
    @Override
    public boolean collect(String noteId, String boardId, String deviceId, String sid) throws IOException {
        LOGGER.info("正在收藏:noteId=" + noteId + "|boardId=" + boardId + "|deviceId=" + deviceId + "|sid=" + sid);

        String queryString = XhsUtils.getCollectQueryString(noteId, boardId, deviceId, sid);
        RequestBody requestBody = RequestBody.create(MediaType.parse(XhsConsts.HTTP_MEDIA_TYPE), queryString);
        Request.Builder builder = new Request.Builder();
        Request request = builder.addHeader("Authorization", sid)
                .addHeader("User-Agent", XhsConsts.HTTP_HEADER_USER_AGENT)
                .addHeader("Host", XhsConsts.HTTP_HEADER_HOST)
                .url(XhsConsts.HOSTNAME + XhsConsts.URL_COLLECT)
                .post(requestBody)
                .build();

        Response response = CLIENT.newCall(request).execute();
        String content = response.body().string();
        LOGGER.info("收藏返回:" + content);
        JSONObject json = JSON.parseObject(content);
        if (json.getIntValue("result") == 0) {
            return true;
        } else {
            throw new RuntimeException(content);
        }
    }

    /**
     * 评论.
     * @param content
     * @param noteId
     * @param deviceId
     * @param sid
     * @return
     * @throws IOException
     */
    @Override
    public boolean comment(String content, String noteId, String deviceId, String sid) throws IOException {
        LOGGER.info("正在评论:noteId=" + noteId + "|content=" + content + "|deviceId=" + deviceId + "|sid=" + sid);

        String queryString = XhsUtils.getCommentQueryString(content, noteId, deviceId, sid);
        RequestBody requestBody = RequestBody.create(MediaType.parse(XhsConsts.HTTP_MEDIA_TYPE), queryString);
        Request.Builder builder = new Request.Builder();
        Request request = builder.addHeader("Authorization", sid)
                .addHeader("User-Agent", XhsConsts.HTTP_HEADER_USER_AGENT)
                .addHeader("Host", XhsConsts.HTTP_HEADER_HOST)
                .url(XhsConsts.HOSTNAME + XhsConsts.URL_COMMENT)
                .post(requestBody)
                .build();

        Response response = CLIENT.newCall(request).execute();
        String ret = response.body().string();
        LOGGER.info("评论返回:" + content);
        JSONObject json = JSON.parseObject(ret);
        if (json.getIntValue("result") == 0) {
            return true;
        } else {
            throw new RuntimeException(ret);
        }
    }

    /**
     * 收藏夹.
     * @param deviceId
     * @param sid
     * @return
     * @throws IOException
     */
    @Override
    public String boardLite(String deviceId, String sid) throws IOException {
        LOGGER.info("正在获取专辑ID:deviceId=" + deviceId + "|sid=" + sid);

        String queryString = XhsUtils.getBoardLiteQueryString(deviceId, sid);
        Request.Builder builder = new Request.Builder();
        Request request = builder.addHeader("Authorization", sid)
                .addHeader("User-Agent", XhsConsts.HTTP_HEADER_USER_AGENT)
                .addHeader("Host", XhsConsts.HTTP_HEADER_HOST)
                .url(XhsConsts.HOSTNAME + XhsConsts.URL_BOARD_LITE + "?" + queryString)
                .get()
                .build();

        Response response = CLIENT.newCall(request).execute();
        String content = response.body().string();
        LOGGER.info("获取专辑返回:" + content);
        JSONObject json = JSON.parseObject(content);
        if (json.getIntValue("result") == 0) {
            JSONArray jsonArray = json.getJSONArray("data");
            return jsonArray.getJSONObject(0).getString("id");
        } else {
            throw new RuntimeException(content);
        }
    }
}
