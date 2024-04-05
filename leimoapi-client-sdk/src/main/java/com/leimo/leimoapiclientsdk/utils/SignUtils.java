package com.leimo.leimoapiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

public class SignUtils {

    public static String getSign(String accessKey, String secretKey) {
        String content = accessKey + "." + secretKey;
        Digester md5 = new Digester(DigestAlgorithm.SHA256);

        return md5.digestHex(content);
    }
}
