package youngpeople.aliali.service;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


/** 카카오 로그인 백엔드로만 구현한 flow
 *  1. 인가코드 받기 (카카오 인증 서버로부터)
 *  2. 토큰 받기 (카카오 인증 서버로부터)
 *  3. 사용자 정보 얻기 (카카오 API 서버로부터)
 */

/**
 *  카카오 로그인 실제 flow
 *  1. 인가코드 받기 (클라이언트로부터) → 프론트엔드의 영역
 *  2. 토큰 받기 (카카오 인증 서버로부터) → 백엔드의 영역
 *  3. 사용자 정보 얻기 (카카오 API 서버로부터) → 백엔드의 영역
 */
@Slf4j
@Service
public class KakaoService {

    @Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.client.secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${kakao.redirect.url}")
    private String KAKAO_REDIRECT_URL;

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";
    private final static String KAKAO_API_URI = "https://kapi.kakao.com";

    public String getKakaoLogin() {
        return KAKAO_AUTH_URI + "/oauth/authorize"
                + "?client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URL
                + "&response_type=code";
    }

    /**
     * 1. 컨트롤러에서 인가코드를 매개변수로 하여 getKakaoInfo를 호출
     * 2. 인가코드를 담아서 restTemplate을 이용하여 POST로 API를 요청하여 응답 받은 후 access 토큰을 얻어냄
     * 3. 응답토큰을 매개변수로 하여 getUserInfoWithToken을 호출
     * 4. 다시 토큰을 담아서 restTemplate을 이용하여 POST로 API를 요청하여 응답 받은 후 유저 정보를 DTO로 변환하여 반환
     */

    /**
     * getKakaoInfo : 인가코드를 이용하여 카카오(kauth)로부터 토큰 획득 후
     * getUserinfoWithToken : 토큰을 이용하여 카카오(kapi)로부터 유저정보를 획득 (getKakaoInfo 메서드 내부에서 호출)
     * @param code (인가코드)
     * @return KakaoDto
     */
    public String getKakaoInfo(String code) throws Exception {
//        log.info("★ call kakao api ★");
        if (code == null) throw new Exception("Failed get authorization code");

        String accessToken = "";
        String refreshToken = "";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type"   , "authorization_code");
        params.add("client_id"    , KAKAO_CLIENT_ID);
        params.add("client_secret", KAKAO_CLIENT_SECRET);
        params.add("code"         , code);
        params.add("redirect_uri" , KAKAO_REDIRECT_URL);

        // Http 통신을 위한 클래스 (RESTfull API 상호작용을 쉽게 할 수 있음)
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        // response : 토큰이 담겨있을 예정 (JSONParser를 사용해서 토큰을 파싱)
        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_AUTH_URI + "/oauth/token",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        // 파싱 도구 (build.gradle 디펜던시 설정 필요)
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());

        accessToken = (String) jsonObject.get("access_token");
        refreshToken = (String) jsonObject.get("refresh_token");

        // 토큰을 파라미터로 하여 유저 정보를 카카오로부터 획득
        return getUserInfoWithToken(accessToken);
    }

    /**
     * 엑세스 토큰을 이용하여 카카오로부터 유저 정보 획득
     * @param accessToken (엑세스토큰)
     * @return KakaoDto
     */
    private String getUserInfoWithToken(String accessToken) throws Exception {

        // 헤더에 토큰 정보 담기
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);

        // response : 유저 정보가 담겨있을 예정
        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_API_URI + "/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        // 유저 정보 파싱
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());
//        JSONObject account = (JSONObject) jsonObject.get("kakao_account");
//        JSONObject profile = (JSONObject) account.get("profile");

        String kakaoId = String.valueOf(jsonObject.get("id"));
//        String nickname = String.valueOf(profile.get("nickname"));

        return kakaoId;
    }

}