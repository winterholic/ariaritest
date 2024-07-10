package youngpeople.aliali.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.clubmember.Bookmark;
import youngpeople.aliali.entity.member.Member;
import youngpeople.aliali.exception.common.NotFoundEntityException;
import youngpeople.aliali.exception.common.NotMatchedEntitiesException;
import youngpeople.aliali.repository.BookmarkRepository;
import youngpeople.aliali.repository.ClubRepository;
import youngpeople.aliali.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;

    public BasicResDto registerBookmark(String kakaoId, Long clubId) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(NotFoundEntityException::new);

        bookmarkRepository.save(new Bookmark(member, club));

        return BasicResDto.builder()
                .message("successful").build();
    }

    public BasicResDto cancelBookmark(String kakaoId, Long bookmarkId) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow(NotFoundEntityException::new);

        if (!member.equals(bookmark.getMember())) {
            throw new NotMatchedEntitiesException();
        }

        bookmarkRepository.delete(bookmark);

        return BasicResDto.builder()
                .message("successful").build();
    }

}
