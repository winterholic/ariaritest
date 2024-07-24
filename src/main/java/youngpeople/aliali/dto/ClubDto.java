package youngpeople.aliali.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import youngpeople.aliali.entity.Image;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.clubmember.Bookmark;
import youngpeople.aliali.entity.clubmember.ClubMember;
import youngpeople.aliali.entity.enumerated.ClubTypeA;
import youngpeople.aliali.entity.enumerated.ClubTypeB;
import youngpeople.aliali.entity.member.School;

import java.util.ArrayList;
import java.util.List;

public class ClubDto {

    @Data
    @NoArgsConstructor
    public static class ClubReqDto {
        private String name;
        private String introduction;
        private ClubTypeA clubTypeA;
        private ClubTypeB clubTypeB;
        private String typeName;
        private Long schoolId;
    }

    public static Club toEntity(ClubReqDto clubReqDto, Image image, School school) {
        return new Club(clubReqDto.getName(), clubReqDto.getIntroduction(),
                clubReqDto.getClubTypeA(), clubReqDto.getClubTypeB(), clubReqDto.getTypeName(), image, school);
    }

    public static void updateEntity(Club club, ClubReqDto clubReqDto, Image image) {
        club.setName(clubReqDto.getName());
        club.setIntroduction(clubReqDto.getIntroduction());
        club.setClubTypeA(clubReqDto.getClubTypeA());
        club.setClubTypeB(clubReqDto.getClubTypeB());
        club.setTypeName(clubReqDto.getTypeName());
        if (image != null) {
            club.setImage(image);
        }
    }

    @Data
    @NoArgsConstructor
    public static class ClubDetailResDto {
        private String message = "successful";
        private Long clubId;
        private String name;
        private String introduction;
        private ClubTypeA clubTypeA;
        private ClubTypeB clubTypeB;
        private String typeName;
        private String imageUrl;
        private Long schoolId;
        private String schoolName;
        private int clubMemberCount;

        public ClubDetailResDto(String message, Long clubId, String name, String introduction, ClubTypeA clubTypeA, ClubTypeB clubTypeB, String typeName, int clubMemberCount) {
            this.message = message;
            this.clubId = clubId;
            this.name = name;
            this.introduction = introduction;
            this.clubTypeA = clubTypeA;
            this.clubTypeB = clubTypeB;
            this.typeName = typeName;
            this.clubMemberCount = clubMemberCount;
        }
    }

    public static ClubDetailResDto fromEntity(Club club, String message) {
        ClubDetailResDto clubDetailResDto = new ClubDetailResDto(message, club.getId(), club.getName(), club.getIntroduction(),
                club.getClubTypeA(), club.getClubTypeB(), club.getTypeName(), club.getClubMembers().size());

        Image image = club.getImage();
        if (image != null) {
            clubDetailResDto.setImageUrl(image.getImageUri());
        }

        School school = club.getSchool();
        if (school != null) {
            clubDetailResDto.setSchoolId(school.getId());
            clubDetailResDto.setSchoolName(school.getName());
        }

        return clubDetailResDto;
    }

    @Data
    @NoArgsConstructor
    public static class ClubsResDto {

        private String message = "successful";
        private List<ClubResDto> clubList = new ArrayList<>();

        public ClubsResDto(String message, List<ClubResDto> clubList) {
            this.message = message;
            this.clubList = clubList;
        }
    }

    @Data
    @NoArgsConstructor
    public static class ClubResDto {
        private Long clubId;
        private String clubName;
        private String schoolName;
        private Long bookmarkId;

        public static ClubResDto fromEntity(Club club, Bookmark bookmark) {
            ClubResDto clubResDto = new ClubResDto();
            clubResDto.setClubId(club.getId());
            clubResDto.setClubName(club.getName());
            if (club.getSchool() != null) {
                clubResDto.setSchoolName(club.getSchool().getName());
            }
            if (bookmark != null) {
                clubResDto.setBookmarkId(bookmark.getId());
            }

            return clubResDto;
        }
    }

}
