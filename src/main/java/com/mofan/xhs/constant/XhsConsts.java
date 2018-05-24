package com.mofan.xhs.constant;

import java.util.HashMap;
import java.util.Map;

public class XhsConsts {
    public static final String PLATFORM = "Android";
    public static final String VERSIONNAME = "8.0.0";
    public static final String CHANNEL = "YingYongBao";
    public static final String LANG = "zh-CN";
    public static final String HTTP_HEADER_USER_AGENT = "Dalvik/4.6.0 (Android 8.0.0;)";
    public static final String HTTP_HEADER_HOST = "www.xiaohongshu.com";
    public static final String HTTP_MEDIA_TYPE = "application/x-www-form-urlencoded";

    public static final String HOSTNAME = "http://www.xiaohongshu.com";
    public static final String URL_FOLLOW = "/api/sns/v1/user/follow";
    public static final String URL_COLLECT = "/api/sns/v1/note/collect";
    public static final String URL_COMMENT = "/api/sns/v3/note/comment";
    public static final String URL_LIKE = "/api/v1/discovery/like";
    public static final String URL_BOARD_LITE = "/api/sns/v2/board/lite";

    public static Map<String, String> params = new HashMap<>();
    static {
        params.put("platform", PLATFORM);
        params.put("versionName", VERSIONNAME);
        params.put("channel", CHANNEL);
        params.put("lang", LANG);
    }

    public static long currentSeconds() {
        return System.currentTimeMillis() / 1000L;
    }
}
