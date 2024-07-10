package youngpeople.aliali.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import youngpeople.aliali.entity.member.Member;

public class MemberDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberUpdateProfileReqDto {
        private Integer profile;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberUpdateNicknameReqDto {
        private String nickname;
    }

    public static void updateProfileEntity(Member member, MemberUpdateProfileReqDto reqDto) {
        member.setProfile(reqDto.getProfile());
    }

    public static void updateNicknameEntity(Member member, MemberUpdateNicknameReqDto reqDto) {
        member.setNickname(reqDto.getNickname());
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberResDto {
        private String message;
        private String nickname;
        private Integer profile;
        private Long schoolId;

        /**
         * from Entity
         */
        public MemberResDto(Member member) {
            this.message  = "successful";
            this.nickname = member.getNickname();
            this.profile = member.getProfile();
            this.schoolId = member.getSchool().getId();
        }
    }

}
