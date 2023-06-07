package com.ray.rayapiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * @author ray 2023/5/19
 */
public class SignUtils {
    public static String getSign(String body, String privateKey) {
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        String content = body + '.' + privateKey;
        return digester.digestHex(content);
    }
}
