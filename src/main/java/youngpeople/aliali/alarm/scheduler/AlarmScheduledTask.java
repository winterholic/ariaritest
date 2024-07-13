package youngpeople.aliali.alarm.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import youngpeople.aliali.alarm.AlarmInfo;
import youngpeople.aliali.entity.club.Recruitment;
import youngpeople.aliali.entity.clubmember.Bookmark;
import youngpeople.aliali.repository.BookmarkRepository;
import youngpeople.aliali.repository.RecruitmentRepository;
import youngpeople.aliali.service.AlarmService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmScheduledTask {

    private final AlarmService alarmService;
    private final RecruitmentRepository recruitmentRepository;
    private final BookmarkRepository bookmarkRepository;

    public void createAlarmForDeadlineImminentRecruitment() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime afterThreeDays = now.plusDays(3);

        List<Recruitment> findRecruitments = recruitmentRepository.findByEndDateBetween(now, afterThreeDays);

        for (Recruitment recruitment : findRecruitments) {
            List<Bookmark> bookmarks = bookmarkRepository.findByClub(recruitment.getClub());
            List<Long> receivers = new ArrayList<>();
            for (Bookmark bookmark : bookmarks) {
                receivers.add(bookmark.getMember().getId());
            }

            AlarmInfo alarmInfo = AlarmInfoCreatorForScheduler.createInfoAtIMMINENT(receivers, recruitment);
            alarmService.createAlarms(alarmInfo);
        }

    }

    public void createAlarmForFinishedRecruitment() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beforeOneDay = now.minusDays(1);

        List<Recruitment> findRecruitments = recruitmentRepository.findByEndDateBetween(beforeOneDay, now);

        for (Recruitment recruitment : findRecruitments) {
            List<Bookmark> bookmarks = bookmarkRepository.findByClub(recruitment.getClub());
            List<Long> receivers = new ArrayList<>();
            for (Bookmark bookmark : bookmarks) {
                receivers.add(bookmark.getMember().getId());
            }

            AlarmInfo alarmInfo = AlarmInfoCreatorForScheduler.createInfoAtFinish(receivers, recruitment);
            alarmService.createAlarms(alarmInfo);
        }

    }

}
