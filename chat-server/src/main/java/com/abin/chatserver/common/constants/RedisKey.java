package com.abin.chatserver.common.constants;

public class RedisKey {

    private static final String BASE_KEY = "jfim:";

    /**
     * 用户信息
     */
    public static final String USER_INFO_STRING = "userInfo:uid_%d";

    /**
     * 用户的信息更新时间
     */
    public static final String USER_MODIFY_STRING = "userModify:uid_%d";

    /**
     * 用户的信息汇总
     */
    public static final String USER_SUMMARY_STRING = "userSummary:uid_%d";

    public static String getKey(String key, Object ... objects) {
        return BASE_KEY + String.format(key, objects);
    }
}
