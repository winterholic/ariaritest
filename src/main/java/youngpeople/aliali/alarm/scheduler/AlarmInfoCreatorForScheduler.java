package youngpeople.aliali.alarm.scheduler;

import youngpeople.aliali.alarm.AlarmInfo;
import youngpeople.aliali.entity.club.Recruitment;

import java.util.List;

public class AlarmInfoCreatorForScheduler {

    private static final String MESSAGE_OF_IMMINENT = "마감이 얼마 남지 않았습니다!";
    private static final String MESSAGE_OF_FINISH = "모집이 마감되었습니다!";

    public static AlarmInfo createInfoAtIMMINENT(List<Long> receivers, Recruitment recruitment) {
        return new AlarmInfo(receivers,
                "/club/" + recruitment.getClub().getId() + "/recruitment/" + recruitment.getId(),
                MESSAGE_OF_IMMINENT);
    }

    public static AlarmInfo createInfoAtFinish(List<Long> receivers, Recruitment recruitment) {
        return new AlarmInfo(receivers,
                "/club/" + recruitment.getClub().getId() + "/recruitment/" + recruitment.getId() + "/apply/list",
                MESSAGE_OF_FINISH);
    }

}
