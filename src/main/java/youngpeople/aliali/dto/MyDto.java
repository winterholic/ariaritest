package youngpeople.aliali.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import youngpeople.aliali.entity.club.Apply;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.clubmember.ClubMember;
import youngpeople.aliali.entity.enumerated.MemberRole;
import youngpeople.aliali.entity.enumerated.ResultType;

import java.util.ArrayList;
import java.util.List;

public class MyDto {

    @Data
    public static class MyClubListResDto {
        private String message;
        private List<MyClubData> managingClubs = new ArrayList<>();
        private List<MyClubData> actingClubs = new ArrayList<>();
        private List<MyClubData> waitingClubs = new ArrayList<>();
        private List<MyClubData> acceptedClubs = new ArrayList<>();

        @Builder
        public MyClubListResDto(String message, List<ClubMember> clubMembers, List<Apply> applies) {
            this.message = message;

            for (ClubMember clubMember : clubMembers) {
                if (clubMember.getMemberRole().equals(MemberRole.GENERAL)) {
                    acceptedClubs.add(new MyClubData(clubMember.getClub(), MyClubStatusType.ACTING));
                } else {
                    managingClubs.add(new MyClubData(clubMember.getClub(), MyClubStatusType.MANAGING));
                }
            }

            for (Apply apply : applies) {
                if (apply.getResultType().equals(ResultType.PENDENCY)) {
                    waitingClubs.add(new MyClubData(apply.getRecruitment().getClub(), MyClubStatusType.WAITING));
                } else {
                    acceptedClubs.add(new MyClubData(apply.getRecruitment().getClub(), MyClubStatusType.ACCEPTED));
                }
            }
        }
    }


    @Data
    public static class MyClubData {
        private Long clubId;
        private String clubName;
        private String clubProfileUri;
        private MyClubStatusType myClubStatusType;

        public MyClubData(Club club, MyClubStatusType myClubStatusType) {
            this.clubId = club.getId();
            this.clubName = club.getName();
            if (club.getImage() != null) {
                this.clubProfileUri = club.getImage().getImageUri();
            }
            this.myClubStatusType = myClubStatusType;
        }
    }


    public enum MyClubStatusType {
        MANAGING,
        ACTING,
        WAITING, // 대기 중
        ACCEPTED // 참여 대기 중
    }

}
