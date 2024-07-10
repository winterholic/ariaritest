package youngpeople.aliali.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;
import youngpeople.aliali.exception.token.NotAuthenticatedException;
import youngpeople.aliali.manager.TokenManager;

import java.io.IOException;

import static youngpeople.aliali.filter.WhiteListConstants.*;

@Slf4j
@AllArgsConstructor
public class MyAuthenticationFilter implements Filter {

    private static final String[] whitelist = {
            "/",
            LOGIN_URL,
            CALLBACK_URL,
            TEST_URL,
            REISSUE_URL,
            SWAGGER_URL,
            SWAGGER_URL2,
            SWAGGER_URL3,
            "/favicon.ico",
            "/my/auth/school-certification",
            "/club/detail/**",
            "/main/clubs/all",
            "/main/clubs/union",
            "/main/recruitments/all",
            "/main/recruitments/union",
            "/main/**"
    };

    private final TokenManager tokenManager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        log.info("URI : {}", requestURI);

        if (!isLoginCheckPath(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        String accessToken = getToken(httpRequest);
        tokenManager.verifyToken(accessToken);

        String kakaoId = tokenManager.getKakaoId(accessToken);
        httpRequest.setAttribute("kakaoId", kakaoId);

        chain.doFilter(request, httpResponse);
    }

    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }

    private String getToken(HttpServletRequest request) {
        String accessToken = request.getHeader("authorization");
        if (accessToken == null) {
            throw new NotAuthenticatedException();
        } else {
            return accessToken;
        }
    }

}
