package youngpeople.aliali.aop.alarm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import youngpeople.aliali.service.AlarmService;

import java.util.List;

import static youngpeople.aliali.aop.alarm.AlarmInfoData.*;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AlarmAspect {

    private final AlarmService alarmService;

    @Pointcut("@annotation(youngpeople.aliali.aop.alarm.AlarmTargetMethod)")
    private void alarmTarget() {}

    @AfterReturning(pointcut = "alarmTarget()",
            returning = "receivers")
    public void callAlarm(JoinPoint joinPoint, List<Long> receivers) throws Throwable {
        AlarmInfo alarmInfo = ALARM_INFO_MAP.get(joinPoint.getSignature().getName());
        for (Long receiver : receivers) {
            alarmService.createAlarms(receiver, alarmInfo);
        }
    }

}
