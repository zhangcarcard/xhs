package com.mofan.xhs.util;

import com.mofan.xhs.constant.XhsConsts;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class XhsUtils {

    public static void collect() {
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(5L,  TimeUnit.SECONDS).build();
        Request.Builder builder = new Request.Builder();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), getCollectQueryString("5affff727ee0a943ccbf9723", "5afbd8b2948d5f24c87c4272", "10cb1a2a-dd3a-3beb-b227-c795c80e3ae1", "session.1211192026120274622"));
        Request request = builder.addHeader("Authorization", "session.1211117351642353967")
                .addHeader("User-Agent", "Dalvik/4.6.0 (Android 8.0.0;)")
                .addHeader("Host", "www.xiaohongshu.com")
                .url(XhsConsts.HOSTNAME + XhsConsts.URL_COLLECT)
                .post(requestBody)
                .build();

        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(Thread.currentThread().getId() + ":" + response.body().string());
            }
        };

        client.newCall(request).enqueue(callback);
    }

    public static void main(String[] args) {
        collect();
    }


    public static void like() {
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(5L,  TimeUnit.SECONDS).build();

        Request.Builder builder = new Request.Builder();
        Request request = builder.addHeader("Authorization", "session.1211117351642353967")
                .addHeader("User-Agent", "Dalvik/4.6.0 (Android 8.0.0;)")
                .addHeader("Host", "www.xiaohongshu.com")
                .url(XhsConsts.HOSTNAME + XhsConsts.URL_LIKE + getLikeQueryString("5afbc654a7c9b83ddadae5a5", "177ab015-e55b-3035-b8ad-a8eb4745b5ad", "session.1211117351642353967"))
                .get()
                .build();

        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(Thread.currentThread().getId() + ":" + response.body().string());
            }
        };

        client.newCall(request).enqueue(callback);
    }

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
        params.put("t", "1526737385"/*String.valueOf(XhsConsts.currentSeconds())*/);

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



    public static String getQueryString(Map<String, String> params) {
        StringBuilder builder = new StringBuilder(512);
        params.forEach((k, v) -> builder.append(k).append("=").append(v).append("&"));
        return builder.toString();
    }
}
