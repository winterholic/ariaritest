package youngpeople.aliali.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.clubmember.ClubMember;
import youngpeople.aliali.entity.enumerated.MemberRole;
import youngpeople.aliali.entity.member.Member;
import youngpeople.aliali.exception.clubmember.ClubMemberRemoveAdminException;
import youngpeople.aliali.exception.clubmember.ClubMemberRoleException;
import youngpeople.aliali.exception.common.NotFoundEntityException;
import youngpeople.aliali.repository.ClubMemberRepository;
import youngpeople.aliali.repository.ClubRepository;
import youngpeople.aliali.repository.MemberRepository;

import static youngpeople.aliali.dto.ClubMemberDto.*;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClubMemberService {

    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;

    public ClubMembersResDto findClubMembers(String kakaoId, Long clubId) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(NotFoundEntityException::new);
        ClubMember myClubMember = clubMemberRepository.findByMemberAndClub(member, club).orElseThrow(NotFoundEntityException::new);

        if (myClubMember.getMemberRole().equals(MemberRole.GENERAL)) {
            throw new ClubMemberRoleException();
        }

        List<ClubMember> clubMembers = club.getClubMembers();

        return new ClubMembersResDto(
                "successful",
                clubMembers);
    }

    public BasicResDto updateClubMembers(String kakaoId, Long clubId,
                                         ClubMembersUpdateReqDto clubMembersUpdateReqDto) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(NotFoundEntityException::new);
        ClubMember myClubMember = clubMemberRepository.findByMemberAndClub(member, club).orElseThrow(NotFoundEntityException::new);

        if (myClubMember.getMemberRole() == MemberRole.GENERAL) {
            throw new ClubMemberRoleException();
        }

        List<ClubMemberReqDto> reqDtos = clubMembersUpdateReqDto.getClubMembers();
        for (ClubMemberReqDto reqDto : reqDtos) {
            ClubMember clubMember = clubMemberRepository.findById(reqDto.getClubMemberId()).get();
            /**
             * 클럽 Admin 이전에 대한 요구사항 필요
             */
            if (clubMember.getMemberRole().equals(MemberRole.ADMIN)) {
                throw new ClubMemberRemoveAdminException();
            }

            clubMember.setMemberRole(reqDto.getMemberRole());
            clubMember.setMemberRoleName(reqDto.getMemberRoleName());
        }

        return BasicResDto.builder()
                .message("successful")
                .build();
    }

    public BasicResDto deleteClubMembers(String kakaoId, Long clubId,
                                         ClubMembersDeleteReqDto clubMembersDeleteReqDto) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(NotFoundEntityException::new);
        ClubMember myClubMember = clubMemberRepository.findByMemberAndClub(member, club).orElseThrow(NotFoundEntityException::new);

        if (myClubMember.getMemberRole().equals(MemberRole.GENERAL)) {
            throw new ClubMemberRoleException();
        }

        List<Long> clubMembersId = clubMembersDeleteReqDto.getClubMembersId();
        for (Long id : clubMembersId) {
            ClubMember clubMember = clubMemberRepository.findById(id).orElseThrow(NotFoundEntityException::new);

            if (clubMember.getMemberRole().equals(MemberRole.ADMIN)) {
                throw new ClubMemberRemoveAdminException();
            }

            clubMemberRepository.delete(clubMember);
        }

        return BasicResDto.builder()
                .message("successful")
                .build();
    }

    /**
     * admin은 탈퇴 불가
     */
    public BasicResDto unregister(String kakaoId, Long clubId) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(NotFoundEntityException::new);
        ClubMember myClubMember = clubMemberRepository.findByMemberAndClub(member, club).orElseThrow(NotFoundEntityException::new);

        if (myClubMember.getMemberRole().equals(MemberRole.ADMIN)) {
            throw new ClubMemberRemoveAdminException();
        }

        clubMemberRepository.delete(myClubMember);

        return BasicResDto.builder()
                .message("successful")
                .build();
    }

}
