package youngpeople.aliali.controller.test;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import youngpeople.aliali.entity.member.Member;
import youngpeople.aliali.repository.MemberRepository;
import youngpeople.aliali.manager.TokenManager;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TokenTestController {

    private final TokenManager tokenManager;
    private final MemberRepository memberRepository;

    private final String EXISTING_KAKAO_ID = "3456718320";
    private final String EXISTING_NAME = "원순재";

    private final String EXISTING_KAKAO_ID_2 = "3458126487";
    private final String EXISTING_NAME_2 = "김대선";

    @GetMapping("/token")
    public TokenDto getToken() {
        String token = tokenManager.issueToken(EXISTING_KAKAO_ID);
        String accessToken = tokenManager.getAccessToken(token);
        String refreshToken = tokenManager.getRefreshToken(token);

        return TokenDto.builder()
                .message("토큰 발급 완료")
                .kakaoId(EXISTING_KAKAO_ID)
                .name(EXISTING_NAME)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @GetMapping("/tokens")
    public TokensDto getTokens() {
        List<Member> all = memberRepository.findAll();

        TokensDto tokensDto = new TokensDto();
        for (Member member : all) {
            String kakaoId = member.getKakaoId();
            String name = member.getNickname();
            String token = tokenManager.issueToken(kakaoId);
            String accessToken = tokenManager.getAccessToken(token);
            String refreshToken = tokenManager.getRefreshToken(token);

            TokenDto dto = TokenDto.builder()
                    .message("x")
                    .kakaoId(kakaoId)
                    .name(name)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            tokensDto.getTokenDtos().add(dto);
        }

        tokensDto.setMessage("전체 회원의 kakaoId와 토큰 발급 완료");
        return tokensDto;
    }

    @GetMapping("/eternal-token")
    public TokenDto getEternalToken() {
        String token = tokenManager.createTokenForTest(EXISTING_KAKAO_ID);
        String accessToken = tokenManager.getAccessToken(token);
        String refreshToken = tokenManager.getRefreshToken(token);

        return TokenDto.builder()
                .message("무제한 토큰 발급 완료")
                .kakaoId(EXISTING_KAKAO_ID)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @GetMapping("/eternal-token-2")
    public TokenDto getEternalToken2() {
        String token = tokenManager.createTokenForTest(EXISTING_KAKAO_ID_2);
        String accessToken = tokenManager.getAccessToken(token);
        String refreshToken = tokenManager.getRefreshToken(token);

        return TokenDto.builder()
                .message("무제한 토큰 발급 완료")
                .kakaoId(EXISTING_KAKAO_ID_2)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TokenDto {
        String message;
        String kakaoId;
        String name;
        String accessToken;
        String refreshToken;
    }


    @Data
    @NoArgsConstructor
    public static class TokensDto {
        String message;
        List<TokenDto> tokenDtos =  new ArrayList<>();
    }

}
