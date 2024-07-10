package youngpeople.aliali.controller;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import youngpeople.aliali.service.KakaoService;
import youngpeople.aliali.service.MemberService;
import youngpeople.aliali.manager.TokenManager;

import static youngpeople.aliali.controller.swagger.SwaggerExplain.*;

/**
 * LoginService 개발하기
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final KakaoService kakaoService;
    private final MemberService memberService;
    private final TokenManager tokenManager;

    @GetMapping("/auth/kakao/callback")
    @LoginKakaoAuthExplain
    public CodeResDto callback(@RequestParam(name = "code") String code) {
        return new CodeResDto(code);
    }

    @PostMapping("/login")
    public LoginResDto login(@RequestParam(name = "code") String code) throws Exception {
        String kakaoId = kakaoService.getKakaoInfo(code);

        String token = memberService.login(kakaoId);
        String accessToken = tokenManager.getAccessToken(token);
        String refreshToken = tokenManager.getRefreshToken(token);

        return LoginResDto.builder()
                .message("successful")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @PostMapping("/reissue")
    @LoginReissueExplain
    public ReissueResDto reissue(@RequestBody ReissueReqDto reissueReqDto) {
        String refreshToken = reissueReqDto.getRefreshToken();

        tokenManager.verifyToken(refreshToken);

        String kakaoId = tokenManager.getKakaoId(refreshToken);
        String token = tokenManager.issueToken(kakaoId);
        String accessToken = tokenManager.getAccessToken(token);

        return ReissueResDto.builder()
                .message("successful")
                .accessToken(accessToken)
                .build();
    }

    @Data
//    @NoArgsConstructor
    @AllArgsConstructor
    public static class CodeResDto {
        private String code;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResDto {
        private String message;
        private String accessToken;
        private String refreshToken;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReissueReqDto {
        private String refreshToken;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReissueResDto {
        private String message;
        private String accessToken;
    }

}
