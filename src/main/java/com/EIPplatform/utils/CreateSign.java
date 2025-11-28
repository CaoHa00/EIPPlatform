package com.EIPplatform.utils;
import org.apache.commons.lang3.StringUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CreateSign {

    public static String createSign(String accessToken, String appId, String keyId, String nonce, String time,
            String appKey) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(accessToken)) {
            sb.append("Accesstoken=").append(accessToken).append("&");
        }
        sb.append("Appid=").append(appId);
        sb.append("&").append("Keyid=").append(keyId);
        sb.append("&").append("Nonce=").append(nonce);
        sb.append("&").append("Time=").append(time).append(appKey);

        String signStr = sb.toString().toLowerCase();
        return MD5_32(signStr);
    }

    private static String MD5_32(String sourceStr) throws Exception {
        String result = "";

        try {
            byte[] b = md5(sourceStr.getBytes("UTF-8"));
            StringBuffer buf = new StringBuffer("");

            for (int offset = 0; offset < b.length; ++offset) {
                int i = b[offset];
                if (i < 0) {
                    i += 256;
                }

                if (i < 16) {
                    buf.append("0");
                }

                buf.append(Integer.toHexString(i));
            }

            result = buf.toString();
        } catch (NoSuchAlgorithmException var6) {
            var6.printStackTrace();
        }

        return result;
    }

    private static byte[] md5(byte[] bytes) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(bytes);
        return md.digest();
    }
}
