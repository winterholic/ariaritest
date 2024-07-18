package youngpeople.aliali.dto;

import lombok.*;
import youngpeople.aliali.entity.member.Alarm;

import java.util.ArrayList;
import java.util.List;

public class AlarmDto {

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

    @Getter @Setter
    @AllArgsConstructor
    public static class AlarmsResDto {
        private String message;
        private List<AlarmResDto> AlarmList;
    }

    public static AlarmsResDto fromEntity(String message, List<Alarm> alarms) {
        List<AlarmResDto> signalList = new ArrayList<>();
        for (Alarm alarm : alarms) {
            signalList.add(new AlarmResDto(alarm));
        }

        return new AlarmsResDto(message, signalList);
    }

}
