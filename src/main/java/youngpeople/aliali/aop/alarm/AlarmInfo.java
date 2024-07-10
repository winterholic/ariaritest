package youngpeople.aliali.aop.alarm;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class AlarmInfo {
    private String uri;
    private String message;
}
