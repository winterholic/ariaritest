package youngpeople.aliali.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.entity.club.Comment;
import youngpeople.aliali.entity.club.Post;
import youngpeople.aliali.entity.member.Block;
import youngpeople.aliali.entity.member.Member;
import youngpeople.aliali.exception.common.NotFoundEntityException;
import youngpeople.aliali.exception.block.BlockedMemberAccessException;
import youngpeople.aliali.repository.CommentRepository;
import youngpeople.aliali.repository.MemberRepository;
import youngpeople.aliali.repository.PostRepository;

import java.util.*;
import java.util.stream.Collectors;

import static youngpeople.aliali.dto.CommentDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Set<Member> findBlockMemberSet(Member targetMember){ //대상 멤버에 대한 blockmember들의 set 반환
        Set<Block> allBlocks = new HashSet<>();
        Optional.ofNullable(targetMember.getBlockings()).ifPresent(allBlocks::addAll);
        Optional.ofNullable(targetMember.getBlockeds()).ifPresent(allBlocks::addAll);

        Set<Member> members = allBlocks.stream()
                .map(Block::getTarget)
                .collect(Collectors.toSet());
        return members;
    }

    public void checkAbleToWriteComment(Member currentMember, Member targetMember){ // 차단 멤버가 댓글 작성 / 게시글 작성 시 예외처리
        Set<Member> blockMemberSet = findBlockMemberSet(targetMember);
        if(blockMemberSet.contains(currentMember)){
            throw new BlockedMemberAccessException();
        }
    }

    public Set<Long> searchBlockComments(List<Comment> originComments, Set<Member> blockedMembers){
        Set<Long> set = new HashSet<>();
        for(Comment parentComment : originComments){
            if(!blockedMembers.contains(parentComment.getMember())){
                set.add(parentComment.getId());
            }
        }
        return set;
    }

    public BasicResDto saveParentComment(CommentReqDto commentReqDto, String kakaoId, Long postId){
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Post post = postRepository.findById(postId).orElseThrow(NotFoundEntityException::new);

        checkAbleToWriteComment(member, post.getMember());

        Comment comment = toEntity(commentReqDto, post, member);
        commentRepository.save(comment);
        return BasicResDto.builder()
                .message("successful")
                .build();
    }

    public BasicResDto saveChildComment(CommentReqDto commentReqDto, String kakaoId, Long postId, Long parentCommentId){
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Post post = postRepository.findById(postId).orElseThrow(NotFoundEntityException::new);
        Comment parentComment = commentRepository.findByIdAndActivatedTrue(parentCommentId).orElseThrow(NotFoundEntityException::new);

        checkAbleToWriteComment(member, parentComment.getMember());

        Comment childComment = toEntity(commentReqDto, post, member, parentComment);
        commentRepository.save(childComment);
        parentComment.addChildComment(childComment);
        commentRepository.save(parentComment);
        return BasicResDto.builder()
                .message("successful")
                .build();
    }

    // 공지사항에 작성된 게시글의 댓글의 경우 모든 댓글 반환
    public NoticeCommentListDto findNoticeCommentList(Long postId){
        List<Comment> comments = commentRepository.findByPostIdAndParentCommentIdIsNull(postId);
        Post post = postRepository.findById(postId).orElseThrow(NotFoundEntityException::new);
        return new NoticeCommentListDto("successful", post, comments);
    }

    public GeneralCommentListDto findGeneralCommentList(Long postId, String kakaoId){
        List<Comment> allComments = commentRepository.findByPostId(postId);

        Member currentMember = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Set<Long> blockedCommentIdSet = searchBlockComments(allComments, findBlockMemberSet(currentMember));

        Post post = postRepository.findById(postId).orElseThrow(NotFoundEntityException::new);
        List<Comment> parentComments = commentRepository.findByPostIdAndParentCommentIdIsNull(postId);

        return new GeneralCommentListDto("successful", post, parentComments, blockedCommentIdSet);
    }
}

