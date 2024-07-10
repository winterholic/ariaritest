package youngpeople.aliali.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import youngpeople.aliali.entity.clubmember.ClubMember;
import youngpeople.aliali.entity.enumerated.MemberRole;

import java.util.ArrayList;
import java.util.List;

public class ClubMemberDto {

    @Data
    @NoArgsConstructor
    public static class ClubMembersResDto {
        private String message;
        private List<ClubMemberResDto> clubMemberList = new ArrayList<>();

        public ClubMembersResDto(String message, List<ClubMember> clubMembers) {
            this.message = message;

            for (ClubMember clubMember : clubMembers) {
                this.clubMemberList.add(new ClubMemberResDto(clubMember));
            }
        }
    }

    @Data
    public static class ClubMemberResDto {
        private Long clubMemberId;
        private String name;
        private MemberRole memberRole;
        private String memberRoleName;

        public ClubMemberResDto(ClubMember clubMember) {
            this.clubMemberId = clubMember.getId();
            this.name = clubMember.getMember().getNickname();
            this.memberRole = clubMember.getMemberRole();
            this.memberRoleName = clubMember.getMemberRoleName();
        }
    }

    /**
     * for updating dto
     */
    @Data
    @NoArgsConstructor
    public static class ClubMembersUpdateReqDto {
        private List<ClubMemberReqDto> clubMembers = new ArrayList<>();
    }

    @Data
    public static class ClubMemberReqDto {
        private Long clubMemberId;
        private MemberRole memberRole;
        private String memberRoleName;
    }

    /**
     * for deleting dto
     */
    @Data
    @NoArgsConstructor
    public static class ClubMembersDeleteReqDto {
        private List<Long> clubMembersId = new ArrayList<>();
    }
}
