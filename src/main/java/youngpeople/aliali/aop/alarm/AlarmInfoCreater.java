package youngpeople.aliali.aop.alarm;

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
import java.util.List;

public class AlarmInfoCreater {

    /**
     * - 알림 대상
     *     1. 댓글 :
     *         1.  게시글 주인 + 멘션된 사람 + 대댓글에 대한 댓글 작성자 (같은 뎁스의 대댓글자에겐 x)
     *     2. 모집 :
     *         1. 모집 마감 : 클럽장 + 관리자 -> 스케줄링
     *         2. 모집 마감 임박 : 클럽 북마크 ( 3일 / 1일 전 ) -> 스케줄링
     *         3. 모집 시작 : 클럽 북마크 -> aop -> ok
     *     3. 지원 :
     *         1. 지원 처리 : 지원자 or 클럽장 + 관리자 -> aop ->
     *         2. 지원 시 : 클럽장 + 관리자 -> aop -> ok
     *     4. 게시글 :
     *         1. 클럽 내 공지사항 / 게시글 : 클럽 내 모든 멤버
     */

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

        ArrayList<Long> receiverIds = new ArrayList<>();

        for (ClubMember clubMember : clubMembers) {
            receiverIds.add(clubMember.getMember().getId());
        }

        return new AlarmInfo(receiverIds,
                "/club/" + recruitment.getClub().getId() + "/recruitment/" + recruitment.getId() + "/apply/list",
                "지원자가 한 명 추가되었습니다!");
    }

    public static AlarmInfo createInfoInJudgeApply(Apply apply) {
        Member member = apply.getMember();
        return new AlarmInfo(new ArrayList<>(Arrays.asList(member.getId())),
                "/my/apply/list",
                "지원이 심사되었습니다!");
    }

    public static AlarmInfo createInfoInJoinClub(ClubMemberRepository clubMemberRepository,
                                                 Apply apply) {
        Club club = apply.getRecruitment().getClub();
        List<ClubMember> clubMembers = clubMemberRepository.findByClubAndMemberRoleNotLike(club, MemberRole.GENERAL);

        ArrayList<Long> receiverIds = new ArrayList<>();
        for (ClubMember clubMember : clubMembers) {
            receiverIds.add(clubMember.getMember().getId());
        }

        return new AlarmInfo(receiverIds,
                "/club/" + club.getId() + "club-members",
                "새로운 멤버가 가입했습니다!");
    }

}
