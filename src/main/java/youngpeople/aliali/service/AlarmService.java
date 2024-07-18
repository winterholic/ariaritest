package youngpeople.aliali.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import youngpeople.aliali.alarm.AlarmInfo;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.entity.member.Alarm;
import youngpeople.aliali.entity.member.Member;
import youngpeople.aliali.exception.common.NotFoundEntityException;
import youngpeople.aliali.exception.common.NotMatchedEntitiesException;
import youngpeople.aliali.repository.MemberRepository;
import youngpeople.aliali.repository.AlarmRepository;

import static youngpeople.aliali.dto.AlarmDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AlarmService {

    private final MemberRepository memberRepository;
    private final AlarmRepository alarmRepository;

    public AlarmsMiniResDto findAlarms(String kakaoId) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        return fromEntityAtMini("successful", member.getAlarms());
    }

    public BasicResDto checkAlarm(String kakaoId, Long alarmId) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(NotFoundEntityException::new);

        if (!member.equals(alarm.getMember())) {
            throw new NotMatchedEntitiesException();
        }

        alarm.setChecked(Boolean.TRUE);

        return BasicResDto.builder()
                .message("successful").build();
    }

    public BasicResDto deleteAlarm(String kakoId, Long alarmId) {
        Member member = memberRepository.findByKakaoId(kakoId).orElseThrow(NotFoundEntityException::new);
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(NotFoundEntityException::new);

        if (!member.equals(alarm.getMember())) {
            throw new NotMatchedEntitiesException();
        }

        alarmRepository.delete(alarm);

        return BasicResDto.builder()
                .message("successful").build();
    }

    public void createAlarms(AlarmInfo alarmInfo) {
        String message = alarmInfo.getMessage();
        String uri = alarmInfo.getUri();

        for (Long receiverId : alarmInfo.getReceiverIds()) {
            Member member = memberRepository.findById(receiverId).orElseThrow(NotFoundEntityException::new);
            Alarm alarm = new Alarm(message, uri, member);
            alarmRepository.save(alarm);
        }
    }

}
