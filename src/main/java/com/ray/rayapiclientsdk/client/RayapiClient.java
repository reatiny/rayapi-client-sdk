package com.ray.rayapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.ray.rayapiclientsdk.domain.User;
import com.ray.rayapiclientsdk.domain.WangzheParams;
import com.ray.rayapiclientsdk.utils.SignUtils;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ray 2023/5/19
 */
public class RayapiClient {

    private static final String GATEWAY_HOST = "http://localhost:8090";
    private String publicKey;
    private String privateKey;

    public RayapiClient(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    /**
     * 添加请求头，发送给网关校验用户的信息
     *
     * @param body 请求体
     * @return map类型的请求头
     */
    private Map<String, String> getHeaderMap(String body) {
        // 解决中文 导致的签名不一致
        String encode = null;
        try {
            encode = URLEncoder.encode(body, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("publicKey", publicKey);
//        一定不能发给后端
//        headers.put("privateKey", privateKey);
        headers.put("nonce", RandomUtil.randomNumbers(4));
        headers.put("body", encode);
        headers.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        headers.put("sign", SignUtils.getSign(encode, privateKey));
        return headers;
    }

    /**
     * post方法获取用户姓名
     * @param name 用户姓名
     * @return 响应体
     */
    public String getNameByGet(String name) {
        // 可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        return HttpUtil.get(GATEWAY_HOST + "/api/name/", paramMap);
    }

    /**
     * post方法获取用户姓名
     * @param name 用户姓名
     * @return 响应体
     */
    public String getNameByPost(String name) {
        // 可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        return HttpUtil.post(GATEWAY_HOST + "/api/name/", paramMap);
    }

    /**
     * post方法获取用户姓名
     * @param user 用户 com.ray.rayapiclientsdk.domain.User
     * @return 响应体
     */
    public String getUserNameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        try (HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute()) {
            System.out.println(httpResponse.getStatus());
            return httpResponse.body();
        }
    }

    public String getChickenSoup() {
        return HttpRequest.get(GATEWAY_HOST +"/api/name/chicken-soup")
                .addHeaders(getHeaderMap(""))
                .execute().body();
    }

    public String getAvatarUrl() {
        return HttpRequest.get(GATEWAY_HOST +"/api/avatar/avatarUrl")
                .addHeaders(getHeaderMap(""))
                .execute().body();
    }

    public String getHeroInfo(WangzheParams wangzheParams) {
        String parameter = JSONUtil.toJsonStr(wangzheParams);
        try (HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/wangzhe")
                .addHeaders(getHeaderMap(parameter))
                .body(parameter)
                .execute()) {
            System.out.println(httpResponse.getStatus());
            return httpResponse.body();
        }
    }

}
