package youngpeople.aliali.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import youngpeople.aliali.controller.swagger.SwaggerAuth;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.service.AlarmService;

import static youngpeople.aliali.dto.AlarmDto.*;

@Slf4j
@RestController
@RequestMapping("/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping("/mini")
    @SwaggerAuth
    public AlarmsMiniResDto getAlarmsMini(HttpServletRequest request) {
        String kakaoId = getKakaoId(request);
        return alarmService.findAlarmsMini(kakaoId);
    }

    @GetMapping("/list")
    @SwaggerAuth
    public AlarmsPageResDto getAlarmsPage(HttpServletRequest request,
                                          @RequestParam(name = "page-idx") int pageIdx) {
        String kakaoId = getKakaoId(request);
        return alarmService.findAlarmsPage(kakaoId, pageIdx);
    }

    @PostMapping("/{alarmId}")
    @SwaggerAuth
    public BasicResDto checkAlarm(HttpServletRequest request, @PathVariable(name = "alarmId") Long alarmId) {
        String kakaoId = getKakaoId(request);
        return alarmService.checkAlarm(kakaoId, alarmId);
    }

    @DeleteMapping ("/{alarmId}")
    @SwaggerAuth
    public BasicResDto deleteAlarm(HttpServletRequest request, @PathVariable(name = "alarmId") Long alarmId) {
        String kakaoId = getKakaoId(request);
        return alarmService.deleteAlarm(kakaoId, alarmId);
    }

    private String getKakaoId(HttpServletRequest request) {
        return (String) request.getAttribute("kakaoId");
    }

}
