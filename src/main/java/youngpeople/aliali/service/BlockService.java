package youngpeople.aliali.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.entity.member.Block;
import youngpeople.aliali.entity.member.Member;
import youngpeople.aliali.exception.block.ExistingBlockException;
import youngpeople.aliali.exception.common.NotFoundEntityException;
import youngpeople.aliali.repository.BlockRepository;
import youngpeople.aliali.repository.MemberRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BlockService {

    private final MemberRepository memberRepository;
    private final BlockRepository blockRepository;

    /**
     * 게시글 댓글 보여줄 때 회원 id를 주실건가요 ?
     */
    public BasicResDto createBlock(String kakaoId, Long targetId) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Member target = memberRepository.findById(targetId).orElseThrow(NotFoundEntityException::new);

        blockRepository.findByMemberAndTarget(member, target).ifPresent(m -> {
            throw new ExistingBlockException();
        });

        Block block = new Block(member, target);
        blockRepository.save(block);

        return BasicResDto.builder()
                .message("successful").build();
    }

}
