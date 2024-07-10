package youngpeople.aliali.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import youngpeople.aliali.entity.member.School;
import youngpeople.aliali.repository.SchoolRepository;

import java.util.ArrayList;
import java.util.List;
import static youngpeople.aliali.dto.SchoolDto.*;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class SchoolService {

    private final SchoolRepository schoolRepository;

    public SchoolsResDto findAll() {

        List<School> schools = schoolRepository.findAll();

        List<SchoolResDto> schoolResDtos = new ArrayList<>();
        for (School school : schools) {
            schoolResDtos.add(new SchoolResDto(school));
        }

        return fromEntities("successful", schools);
    }

}
