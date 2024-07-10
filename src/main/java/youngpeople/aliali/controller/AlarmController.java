package youngpeople.aliali.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import youngpeople.aliali.controller.swagger.SwaggerAuth;
import youngpeople.aliali.service.AlarmService;

import static youngpeople.aliali.dto.AlarmDto.*;

@Slf4j
@RestController
@RequestMapping("/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping("/list")
    @SwaggerAuth
    public AlarmsResDto getSignals(HttpServletRequest request) {
        String kakaoId = getKakaoId(request);
        return alarmService.findAlarms(kakaoId);
    }

    /**
     * 개발 중
     */
    @GetMapping("/{alarmId}")
    @SwaggerAuth
    public void checkSignal(HttpServletRequest request, @PathVariable(name = "alarmId") Long alarmId) {
        String kakaoId = getKakaoId(request);

    }

    @DeleteMapping ("/{alarmId}")
    @SwaggerAuth
    public void deleteSignal(HttpServletRequest request, @PathVariable(name = "alarmId") Long alarmId) {
        String kakaoId = getKakaoId(request);
    }

    private String getKakaoId(HttpServletRequest request) {
        return (String) request.getAttribute("kakaoId");
    }

}
