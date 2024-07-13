package youngpeople.aliali.alarm.aop;

import youngpeople.aliali.alarm.AlarmInfo;
import youngpeople.aliali.entity.club.Apply;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.club.Recruitment;
import youngpeople.aliali.entity.clubmember.Bookmark;
import youngpeople.aliali.entity.clubmember.ClubMember;
import youngpeople.aliali.entity.enumerated.MemberRole;
import youngpeople.aliali.entity.member.Member;
import youngpeople.aliali.repository.BookmarkRepository;
import youngpeople.aliali.repository.ClubMemberRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AlarmInfoCreatorForAop {

    public static AlarmInfo createInfoInRegisterRecruitment(BookmarkRepository bookmarkRepository,
                                                            Recruitment recruitment) {
        List<Bookmark> bookmarks = bookmarkRepository.findByClub(recruitment.getClub());

        List<Long> receiverIds = new ArrayList<>();
        for (Bookmark bookmark : bookmarks) {
            receiverIds.add(bookmark.getMember().getId());
        }
        return new AlarmInfo(receiverIds,
                "/club/" + recruitment.getClub().getId() + "/recruitment/" + recruitment.getId(),
                "관심있는 동아리가 모집을 시작했습니다!");
    }

    public static AlarmInfo createInfoInRegisterApply(ClubMemberRepository clubMemberRepository,
                                                      Recruitment recruitment) {
        List<ClubMember> clubMembers = clubMemberRepository.findByClubAndMemberRoleNotLike(recruitment.getClub(), MemberRole.GENERAL);

        List<Long> receiverIds = new ArrayList<>();
        for (ClubMember clubMember : clubMembers) {
            receiverIds.add(clubMember.getMember().getId());
        }
        return new AlarmInfo(receiverIds,
                "/club/" + recruitment.getClub().getId() + "/recruitment/" + recruitment.getId() + "/apply/list",
                "지원자가 한 명 추가되었습니다!");
    }

    public static AlarmInfo createInfoInJudgeApplies(List<Apply> applies) {
        List<Long> receiverIds = new ArrayList<>();
        for (Apply apply : applies) {
            receiverIds.add(apply.getMember().getId());
        }

        return new AlarmInfo(receiverIds,
                "/my/apply/list",
                "지원이 심사되었습니다!");
    }

    public static AlarmInfo createInfoInJoinClub(ClubMemberRepository clubMemberRepository,
                                                 Apply apply) {
        Club club = apply.getRecruitment().getClub();
        List<ClubMember> clubMembers = clubMemberRepository.findByClubAndMemberRoleNotLike(club, MemberRole.GENERAL);

        List<Long> receiverIds = new ArrayList<>();
        for (ClubMember clubMember : clubMembers) {
            receiverIds.add(clubMember.getMember().getId());
        }

        return new AlarmInfo(receiverIds,
                "/club/" + club.getId() + "club-members",
                "새로운 멤버가 가입했습니다!");
    }

}
