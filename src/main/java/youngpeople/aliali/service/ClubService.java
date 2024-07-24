package youngpeople.aliali.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.entity.Image;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.clubmember.Bookmark;
import youngpeople.aliali.entity.clubmember.ClubMember;
import youngpeople.aliali.entity.enumerated.ImageTargetType;
import youngpeople.aliali.entity.enumerated.MemberRole;
import youngpeople.aliali.entity.member.Member;
import youngpeople.aliali.entity.member.School;
import youngpeople.aliali.exception.club.ClubDeleteException;
import youngpeople.aliali.exception.clubmember.ClubMemberRoleException;
import youngpeople.aliali.exception.club.ExistingClubNameException;
import youngpeople.aliali.exception.common.NotFoundEntityException;
import youngpeople.aliali.manager.ImageManager;
import youngpeople.aliali.repository.*;

import java.util.ArrayList;
import java.util.List;

import static youngpeople.aliali.dto.ClubDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClubService {

    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final SchoolRepository schoolRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ImageRepository imageRepository;

    private final ImageManager imageManager;

    public ClubDetailResDto findClub(Long clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(NotFoundEntityException::new);
        return fromEntity(club, "successful");
    }

    public BasicResDto registerClub(ClubReqDto clubReqDto, String kakaoId, MultipartFile imageFile) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        School school = schoolRepository.findById(clubReqDto.getSchoolId()).orElse(null);

        if (school != null) {
            clubRepository.findBySchoolAndName(school, clubReqDto.getName()).ifPresent(m -> {
                throw new ExistingClubNameException();
            });
        }

        Image image = null;
        if (imageFile != null) {
            String imageUrl = imageManager.imageSave(imageFile);
            image = new Image(ImageTargetType.CLUB, imageUrl);
        }

        Club club = toEntity(clubReqDto, image, school);
        clubRepository.save(club);
        if (image != null) {
            image.setClub(club);
            imageRepository.save(image);
        }
        ClubMember clubMember = new ClubMember(member, club, MemberRole.ADMIN, "master");
        clubMemberRepository.save(clubMember);

        return BasicResDto.builder()
                .message("successful")
                .build();
    }

    public BasicResDto updateClub(Long clubId, ClubReqDto clubReqDto, String kakaoId, MultipartFile imageFile) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(NotFoundEntityException::new);

        ClubMember clubMember = clubMemberRepository.findByMemberAndClub(member, club).orElseThrow(NotFoundEntityException::new);
        if (clubMember.getMemberRole().equals(MemberRole.GENERAL)) {
            throw new ClubMemberRoleException();
        }

        Image image = null;
        if (imageFile != null) {
            String imageUrl = imageManager.imageSave(imageFile);
            image = new Image(ImageTargetType.CLUB, imageUrl);
        }

        updateEntity(club, clubReqDto, image);

        return BasicResDto.builder()
                .message("successful")
                .build();
    }

    public BasicResDto deleteClub(String kakaoId, Long clubId) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(NotFoundEntityException::new);
        ClubMember clubMember = clubMemberRepository.findByMemberAndClub(member, club).orElseThrow(NotFoundEntityException::new);

        if (!clubMember.getMemberRole().equals(MemberRole.ADMIN)) {
            throw new ClubMemberRoleException();
        }

        if (club.getClubMembers().size() > 1) {
            throw new ClubDeleteException();
        }

        clubRepository.delete(club);

        return BasicResDto.builder()
                .message("successful")
                .build();
    }

}
