package youngpeople.aliali.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import youngpeople.aliali.entity.member.School;

import java.util.ArrayList;
import java.util.List;

public class SchoolDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SchoolAuthReqDto {
        Long schoolId;
        String email;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SchoolsResDto {
        private String message;
        private List<SchoolResDto> schools = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    public static class SchoolResDto {
        private Long id;
        private String name;

        public SchoolResDto(School school) {
            id = school.getId();
            name = school.getName();
        }
    }

    public static SchoolsResDto fromEntities(String message, List<School> schools) {
        List<SchoolResDto> schoolResDtos = new ArrayList<>();
        for (School school : schools) {
            schoolResDtos.add(new SchoolResDto(school));
        }

        return new SchoolsResDto(message, schoolResDtos);
    }

}
