package youngpeople.aliali.dto;

import lombok.*;
import org.springframework.data.domain.Page;
import youngpeople.aliali.entity.member.Alarm;

import java.util.ArrayList;
import java.util.List;

public class AlarmDto {


    @Getter @Setter
    @AllArgsConstructor
    public static class AlarmsPageResDto {
        private String message;
        private List<AlarmResDto> alarmList;
        private int totalPages;
    }

    public static AlarmsPageResDto fromEntityAtPage(String message, Page<Alarm> page) {
        List<Alarm> alarms = page.getContent();

        List<AlarmResDto> alarmList = new ArrayList<>();
        for (Alarm alarm : alarms) {
            alarmList.add(new AlarmResDto(alarm));
        }

        return new AlarmsPageResDto(message, alarmList, page.getTotalPages());
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class AlarmsMiniResDto {
        private String message;
        private List<AlarmResDto> alarmList;
    }

    public static AlarmsMiniResDto fromEntityAtMini(String message, List<Alarm> alarms) {
        List<AlarmResDto> signalList = new ArrayList<>();
        for (Alarm alarm : alarms) {
            signalList.add(new AlarmResDto(alarm));
        }
        return new AlarmsMiniResDto(message, signalList);
    }

    public static class AlarmResDto {
        private String text;
        private String url;
        private Boolean checked;

        public AlarmResDto(Alarm alarm) {
            this.text = alarm.getMessage();
            this.url = alarm.getUrl();
            this.checked = alarm.getChecked();
        }
    }

}
