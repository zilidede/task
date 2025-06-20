package com.zl.utils.other;

import java.util.UUID;

/**
 * @Description: 生成uuid
 * @Param:
 * @Auther: zl
 * @Date: 2020/3/27 21:58
 */
public class UuidUtils {
    private static final UuidUtils singleton = new UuidUtils();

    private UuidUtils() {
    }

    public static UuidUtils getInstance() {
        return singleton;
    }

    public static void main(String[] args) {
        UuidUtils obj = new UuidUtils();
        int i = 0;
        while (i < 1) {
            System.out.println(UuidUtils.getInstance().getUuidGUID(13));
            i++;
        }
    }

    private UUID getUuid() {
        return UUID.randomUUID();//用来生成数据库的主键id
    }

    private String getUuidGUID() {
        UUID uuid = getUuid();
        String[] s = uuid.toString().split("-");
        String temp = "";
        for (String s1 : s) {
            Long l = Long.parseLong(s1, 16);
            temp = temp + l;
        }
        return temp;
    }

    public Long getUuidGUID(int len) {
        //用来生成数据库的主键id
        if (len > 18)
            return -1L;
        String uuid = getUuidGUID();
        return Long.parseLong(uuid.substring(0, len));
    }

    public Integer getUuidGUIDInt(int len) {
        //用来生成数据库的主键id
        if (len >= 8)
            return -1;
        String uuid = getUuidGUID();
        return Integer.parseInt(uuid.substring(0, len));
    }

}
