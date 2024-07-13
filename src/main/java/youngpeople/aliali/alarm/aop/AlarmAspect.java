package youngpeople.aliali.alarm.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import youngpeople.aliali.alarm.AlarmInfo;
import youngpeople.aliali.service.AlarmService;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AlarmAspect {

    private final AlarmService alarmService;

    @Pointcut("@annotation(youngpeople.aliali.alarm.aop.AlarmTargetMethod)")
    private void alarmTarget() {}

    @AfterReturning(pointcut = "alarmTarget()",
            returning = "alarmInfo")
    public void callAlarm(AlarmInfo alarmInfo) {
        alarmService.createAlarms(alarmInfo);
    }

}