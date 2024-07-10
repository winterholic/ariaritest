package youngpeople.aliali.manager;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import youngpeople.aliali.exception.token.IllegalTokenException;
import youngpeople.aliali.exception.token.TimeoutTokenException;

import java.util.Date;

/**
 * 1. JwtToken 생성 : createAccessToken
 * 2. JwtToken 분석 : verifyToken
 *
 * auth0 라이브러리 사용
 */
@Slf4j
@Component
public class TokenManager {

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${security.jwt.accees-expiration}")
    private Long accessExpiration;

    private static final Long refreshExpiration = 3600000L * 24 * 360;

    @Value("${security.secret-key}")
    private String secretKey;

    private static final String SEPERATE_VALUE = "/////";

    public String issueToken(String kakaoId) {
        String accessToken = createToken(kakaoId, accessExpiration);
        String refreshToken = createToken(kakaoId, refreshExpiration);

        return accessToken + SEPERATE_VALUE + refreshToken;
    }

    public String createToken(String kakaoId, Long expiration) {
        Algorithm algorithm = Algorithm.HMAC512(secretKey);
        Date expiresDate = new Date(System.currentTimeMillis() + expiration);

        return JWT.create()
                .withIssuer(issuer)
                .withSubject(kakaoId)
                .withClaim("kakaoId", kakaoId)
                .withExpiresAt(expiresDate)
                .sign(algorithm);
    }

    //    성공 시 true 반환 및 log 출력 | 실패 시 exception 발생
    public void verifyToken(String accessToken) {
        Algorithm algorithm = Algorithm.HMAC512(secretKey);
        log.info(" TokenService : Verifying token started ");

        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();

            // 예외 케이스 2가지 : 잘못된 토큰 / 타임아웃
            DecodedJWT verify = verifier.verify(accessToken);
            log.info(" TokenService : Verifying is successful ");

        } catch (TokenExpiredException e) {
            throw new TimeoutTokenException();
        } catch (JWTVerificationException e) {
            log.info("illegal token exception", e);
            throw new IllegalTokenException();
        }

    }

    public String getKakaoId(String accessToken) {
        return JWT.decode(accessToken).getClaim("kakaoId").asString();
    }

    public String getAccessToken(String token) {
        return token.split(SEPERATE_VALUE)[0];
    }

    public String getRefreshToken(String token) {
        return token.split(SEPERATE_VALUE)[1];
    }

    /**
     * for school certification
     */
    public String createAuthToken(String kakaoId, Long schoolId) {
        Algorithm algorithm = Algorithm.HMAC512(secretKey);
        Date date = new Date(System.currentTimeMillis() + 300000);

        String authToken = JWT.create()
                .withIssuer(issuer)
                .withSubject(kakaoId)
                .withClaim("kakaoId", kakaoId)
                .withClaim("schoolId", String.valueOf(schoolId))
                .withExpiresAt(date)
                .sign(algorithm);

        return authToken;
    }

    public void verifyAuthToken(String authToken) {
        Algorithm algorithm = Algorithm.HMAC512(secretKey);
        log.info(" authToken verifying ");

        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();

            verifier.verify(authToken);

        } catch (TokenExpiredException e) {
            throw new TimeoutTokenException();
        } catch (JWTVerificationException e) {
            log.info("illegal token exception", e);
            throw new IllegalTokenException();
        }

    }

    public String getSchoolId(String authToken) {
        return JWT.decode(authToken).getClaim("schoolId").asString();
    }

    /**
     * 편리한 테스트를 위한 긴 만료시간을 가진 토큰 발급
     */
    public String createTokenForTest(String kakaoId) {
        Algorithm algorithm = Algorithm.HMAC512(secretKey);
        Date refreshDate = new Date(System.currentTimeMillis() + refreshExpiration);

        String accessToken = JWT.create()
                .withIssuer(issuer)
                .withSubject(kakaoId)
                .withClaim("kakaoId", kakaoId)
                .withExpiresAt(refreshDate)
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withIssuer(issuer)
                .withSubject(kakaoId)
                .withClaim("kakaoId", kakaoId)
                .withExpiresAt(refreshDate)
                .sign(algorithm);

        return accessToken + SEPERATE_VALUE + refreshToken;
    }
}