package youngpeople.aliali.alarm.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerConfig {

    private final AlarmScheduledTask alarmScheduledTask;

    @Scheduled(cron = "0 0 20 * * *")
    public void run1() {
        try {
            alarmScheduledTask.createAlarmForDeadlineImminentRecruitment();
        } catch (Exception e) {
            log.error("Scheduled Error [alarmScheduledTask.createAlarmForDeadlineImminentRecruitment()]", e);
        }
    }

    @Scheduled(cron = "0 0 20 * * *")
    public void run2() {
        try {
            alarmScheduledTask.createAlarmForFinishedRecruitment();
        } catch (Exception e) {
            log.error("Scheduled Error [alarmScheduledTask.createAlarmForDeadlineImminentRecruitment()]", e);
        }
    }


}
