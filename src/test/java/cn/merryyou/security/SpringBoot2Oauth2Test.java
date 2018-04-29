package cn.merryyou.security;

import cn.merryyou.security.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created on 2018/4/28.
 *
 * @author zlf
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SpringBoot2Oauth2Test {
    //端口
    final static long PORT = 8888;
    //clientId
    final static String CLIENT_ID = "merryyou";
    //clientSecret
    final static String CLIENT_SECRET = "merryyou";
    //用户名
    final static String USERNAME = "admin";
    //密码
    final static String PASSWORD = "123456";
    //获取accessToken得URI
    final static String TOKEN_REQUEST_URI = "http://127.0.0.1:" + PORT + "/oauth/token?grant_type=password&username=" + USERNAME + "&password=" + PASSWORD + "&scope=all";
    //获取用户信息得URL
    final static String USER_INFO_URI = "http://127.0.0.1:" + PORT + "/userRedis";
    //登录地址
    final static String SIGN_IN_URI = "http://127.0.0.1:" + PORT + "/login";

    @Test
    public void getUserInfo() throws Exception {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "bearer " + getAccessToken());
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        // pay attention, if using get with headers, should use exchange instead of getForEntity / getForObject
        ResponseEntity<String> result = rest.exchange(USER_INFO_URI, HttpMethod.GET, entity, String.class, new Object[]{null});
        log.info("用户信息返回的结果={}", JsonUtil.toJson(result));
    }

    /**
     * 用户名密码登录
     * @throws Exception
     */
    @Test
    public void signInTest() throws Exception {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("authorization", getBasicAuthHeader());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "admin");
        params.add("password", "123456");

        HttpEntity<?> entity = new HttpEntity(params, headers);
        // pay attention, if using get with headers, should use exchange instead of getForEntity / getForObject
        ResponseEntity<String> result = rest.exchange(SIGN_IN_URI, HttpMethod.POST, entity, String.class, new Object[]{null});
        log.info("登录信息返回的结果={}", JsonUtil.toJson(result));
    }

    /**
     * 获取accessToken
     *
     * @return
     */
    private String getAccessToken() {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("authorization", getBasicAuthHeader());
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<OAuth2AccessToken> resp = rest.postForEntity(TOKEN_REQUEST_URI, entity, OAuth2AccessToken.class);
        if (!resp.getStatusCode().equals(HttpStatus.OK)) {
            throw new RuntimeException(resp.toString());
        }
        OAuth2AccessToken t = resp.getBody();
        log.info("accessToken={}", JsonUtil.toJson(t));
        log.info("the response, access_token: " + t.getValue() + "; token_type: " + t.getTokenType() + "; "
                + "refresh_token: " + t.getRefreshToken() + "; expiration: " + t.getExpiresIn() + ", expired when:" + t.getExpiration());
        return t.getValue();

    }

    /**
     * 构建header
     *
     * @return
     */
    private String getBasicAuthHeader() {
        String auth = CLIENT_ID + ":" + CLIENT_SECRET;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        return authHeader;
    }
}
