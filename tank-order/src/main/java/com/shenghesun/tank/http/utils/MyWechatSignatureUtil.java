package com.shenghesun.tank.http.utils;

import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;

public class MyWechatSignatureUtil {
	/**
     * 1. 将token、timestamp、nonce三个参数进行字典序排序
     * 2. 将三个参数字符串拼接成一个字符串进行sha1加密
     * 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
     *
     * @param signature 期望值
     * @param textArr text array
     * @return 验证结果
     */
    public static boolean checkSignature(final String signature, final String... textArr) {
        Arrays.sort(textArr);
        final StringBuilder sb = new StringBuilder();
        for (final String s : textArr) {
            sb.append(s);
        }
        final String plainText = sb.toString();
        return DigestUtils.sha1Hex(plainText).equals(signature);
    }
}
