package youngpeople.aliali.aop.alarm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import youngpeople.aliali.service.AlarmService;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AlarmAspect {

    private final AlarmService alarmService;

    @Pointcut("@annotation(youngpeople.aliali.aop.alarm.AlarmTargetMethod)")
    private void alarmTarget() {}

    @AfterReturning(pointcut = "alarmTarget()",
            returning = "alarmInfo")
    public void callAlarm(JoinPoint joinPoint, AlarmInfo alarmInfo) throws Throwable {
        alarmService.createAlarms(alarmInfo);
    }

}
