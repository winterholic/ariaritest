package youngpeople.aliali.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import youngpeople.aliali.service.MyService;

import static youngpeople.aliali.dto.MyDto.*;

/**
 * 나와 관련된 클럽을 전부 주는데
 * 관리중 / 활동중 / 지원한 / 참여 대기
 * 줄 데이터 : clubId / clubName / clubProfile / 클럽에서의 내 위치
 */
@Slf4j
@RestController
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyController {

    private final MyService myService;

    @GetMapping("/club/list")
    public MyClubListResDto getMyClubs(HttpServletRequest request) {
        String kakaoId = getKakaoId(request);

        return myService.findMyClubs(kakaoId);
    }



    private String getKakaoId(HttpServletRequest request) {
        return (String) request.getAttribute("kakaoId");
    }

}
