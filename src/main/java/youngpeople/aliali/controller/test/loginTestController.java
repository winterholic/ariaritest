package youngpeople.aliali.controller.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import youngpeople.aliali.service.KakaoService;

@Slf4j
@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
public class loginTestController {

    private final KakaoService kakaoService;

    @GetMapping("/login-page")
    public String login(Model model) {
        model.addAttribute("location", kakaoService.getKakaoLogin());
        log.info(" TestController : return login page ");
        return "login";
    }
}
