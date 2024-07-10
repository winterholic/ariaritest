package youngpeople.aliali.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import youngpeople.aliali.manager.TokenManager;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class TokenManagerTest {

    @Autowired
    TokenManager tokenManager;

    private String EXISTING_KAKAO_ID = "3456718320";
    private Long EXISTING_SCHOOL_ID = 1L;

    @Test
    void 토큰_생성_및_검증() {
        String kakaoId = "123456";
        String token = tokenManager.issueToken(kakaoId);

        String accessToken = tokenManager.getAccessToken(token);

        tokenManager.verifyToken(accessToken);

    }

    @Test
    void 토큰에서_카카오아이디_조회() {
        String kakaoId = "19961119";

        String token = tokenManager.issueToken(kakaoId);
        String accessToken = tokenManager.getAccessToken(token);
        String findKakaoId = tokenManager.getKakaoId(accessToken);

        log.info("kakaoId = {}", kakaoId);
        assertThat(findKakaoId).isEqualTo(kakaoId);
    }

    /**
     * school-auth 추가 필요
     */


}