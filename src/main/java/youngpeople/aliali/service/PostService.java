package youngpeople.aliali.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.dto.PostDto;
import youngpeople.aliali.entity.Image;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.club.Comment;
import youngpeople.aliali.entity.club.Post;
import youngpeople.aliali.entity.enumerated.ImageTargetType;
import youngpeople.aliali.entity.enumerated.PostType;
import youngpeople.aliali.entity.member.Block;
import youngpeople.aliali.entity.member.Member;
import youngpeople.aliali.exception.common.NotFoundEntityException;
import youngpeople.aliali.exception.post.BlockedMemberAccessException;
import youngpeople.aliali.exception.post.FixedCountException;
import youngpeople.aliali.manager.ImageManager;
import youngpeople.aliali.repository.*;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static youngpeople.aliali.dto.PostDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final ImageManager imageManager;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;

    public Boolean isFixable(Long clubId) {
        //return (postRepository.findByClubIdAndFixedAndPostType(clubId, true, PostType.NOTICE).size() < 3);
        return postRepository.CountByClubIdAndFixedAndPostType(clubId, true, PostType.NOTICE) < 3; // 고정개수 3개 이하인지 검사
    }

    public void checkFixablePostAtSaving(PostReqDto postReqDto, Long clubId) { // 게시글 작성 시 예외처리
        if (postReqDto.getFixed() && !isFixable(clubId)) {
            throw new FixedCountException();
        }
    }

    public void checkFixablePostAtModifying(PostReqDto postReqDto, Long clubId, Post originPost){ // 게시글 수정 시 예외처리
        if(postReqDto.getFixed() && !originPost.getFixed() && !isFixable(clubId)){
            throw new FixedCountException();
        }
    }

    public List<Image> saveImageWithPost(List<MultipartFile> imageFiles, Post post){ // 이미지 저장처리
        List<Image> images = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            Image image = null;
            if (imageFile != null) {
                String imageUrl = imageManager.imageSave(imageFile);
                image = new Image(ImageTargetType.POST, imageUrl);
            }
            images.add(image);
            if (image != null) {
                image.setPost(post);
                imageRepository.save(image);
            }
        }
        return images;
    }

    public void softDeleteImageData(Post originPost){
        List<Image> images = originPost.getImages();
        for (Image image : images) {
            image.setActivated(false);
        }
        imageRepository.saveAll(images); // 테스트해보고 필요없으면 지움
    }

    public List<Member> findBlockMember(Member targetMember) {
        List<Block> allBlocks = new ArrayList<>();
        Optional.ofNullable(targetMember.getBlockings()).ifPresent(allBlocks::addAll);
        Optional.ofNullable(targetMember.getBlockeds()).ifPresent(allBlocks::addAll);
        List<Member> members = new ArrayList<>();
        for(Block block : allBlocks){
            members.add(block.getTarget());
        }
        return members;
    }

    public Set<Member> findBlockMemberSet(Member targetMember){
        Set<Block> allBlocks = new HashSet<>();
        Optional.ofNullable(targetMember.getBlockings()).ifPresent(allBlocks::addAll);
        Optional.ofNullable(targetMember.getBlockeds()).ifPresent(allBlocks::addAll);

        Set<Member> members = allBlocks.stream()
                .map(Block::getTarget)
                .collect(Collectors.toSet());
        return members;
    }

    public void checkAbleToFindPost(Member currentMember, Member postAuthor){
        Set<Member> blockMemberSet = findBlockMemberSet(postAuthor);
        if(blockMemberSet.contains(currentMember)){
            throw new BlockedMemberAccessException();
        }
    }










    public BasicResDto savePost(PostReqDto postReqDto, Long clubId, PostType postType, String kakaoId, List<MultipartFile> imageFiles) {
        checkFixablePostAtSaving(postReqDto, clubId);

        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(NotFoundEntityException::new);
        Post post = toEntity(postReqDto, club, member, postType);

        List<Image> images = saveImageWithPost(imageFiles, post);
        post.setImages(images);
        postRepository.save(post);

        return BasicResDto.builder()
                .message("successful")
                .build();
    }



    public BasicResDto modifyPost(PostReqDto postReqDto, Long clubId, Long postId, List<MultipartFile> imageFiles){
        Post originPost = postRepository.findById(postId).orElseThrow(NotFoundEntityException::new);

        checkFixablePostAtModifying(postReqDto, clubId, originPost);

        softDeleteImageData(originPost); // 이미지가 변경 되었는지 알 수 있는 데이터를 함께 받는다면, 좀 더 정밀한 코드개선이 필요

        List<Image> images = saveImageWithPost(imageFiles, originPost);
        updateEntity(postReqDto, originPost, images);

        return BasicResDto.builder()
                .message("successful")
                .build();
    }

    public MainPagePostListDto mainClubPagePostList(Long clubId){
        int pageSize = 10;
        Pageable pageRequest = PageRequest.of(0, pageSize);
        Page<Post> normalPage = postRepository.findByClubIdAndPostTypeAndFixedOrderByCreatedDateDesc(clubId, PostType.GENERAL, false, pageRequest);
        List<Post> posts = normalPage.hasContent() ? normalPage.getContent() : List.of();
        return new MainPagePostListDto("successful", posts);
    }

    public NoticePostListDto findNoticePostList(Long clubId, int pageIdx){
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageIdx - 1, pageSize);

        Page<Post> normalPage = postRepository.findByClubIdAndPostTypeAndFixedOrderByCreatedDateDesc(clubId, PostType.NOTICE, false, pageRequest);
        List<Post> normalPosts = normalPage.hasContent() ? normalPage.getContent() : List.of();

        Page<Post> fixedPage = postRepository.findByClubIdAndPostTypeAndFixedOrderByCreatedDateDesc(clubId, PostType.NOTICE, true, PageRequest.of(0, 3));
        List<Post> fixedPosts = fixedPage.hasContent() ? fixedPage.getContent() : List.of();

        return new NoticePostListDto("successful", normalPosts, fixedPosts);
    }

    // findNoticeCommentList구현예정

    public ImageListDto imageList(Long clubId, int pageIdx, int pageSize){
        PageRequest pageRequest = PageRequest.of(pageIdx, pageSize);
        List<Image> images = imageRepository.findByClubIdOrderByCreatedDateDesc(clubId, pageRequest);
        return new ImageListDto("successful", images);
    }

    public GeneralPostListDto findGeneralPostList(String kakaoId, Long clubId, int pageIdx){
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageIdx - 1, pageSize);
        Member currentMember = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Page<Post> page = postRepository.findByMemberNotInAndClubIdAndPostTypeOrderByCreatedDateDesc(findBlockMember(currentMember), clubId, PostType.GENERAL, pageRequest);
        List<Post> posts = page.hasContent() ? page.getContent() : List.of();
        return new GeneralPostListDto("successful", posts);
    }


    public PostDetailDto findDetailPost(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(NotFoundEntityException::new);
        return new PostDetailDto("successful", post);
    }

    public PostDetailDto filterBlockMembersDetailPost(PostDetailDto postDetailDto, String kakaoId, Long postId){
        Post post = postRepository.findById(postId).orElseThrow(NotFoundEntityException::new);
        // 차단된 유저 조회 제한기능 개선 필수
        Member postAuthor = memberRepository.findByKakaoId(post.getMember().getKakaoId()).orElseThrow(NotFoundEntityException::new);
        Member currentMember = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        checkAbleToFindPost(currentMember, postAuthor);
        return postDetailDto;
    }

    public BasicResDto saveParentComment(CommentReqDto commentReqDto, String kakaoId, Long postId){
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Post post = postRepository.findById(postId).orElseThrow(NotFoundEntityException::new);
        // 애초에 차단한 유저는 보이지가 않기 때문에 따로 구현안해도 될 것 같음(?)
        Comment comment = toEntity(commentReqDto, post, member);
        commentRepository.save(comment);
        return BasicResDto.builder()
                .message("successful")
                .build();
    }

    public BasicResDto saveChildComment(CommentReqDto commentReqDto, String kakaoId, Long postId, Long parentCommentId){
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Post post = postRepository.findById(postId).orElseThrow(NotFoundEntityException::new);
        // 애초에 차단한 유저는 보이지가 않기 때문에 따로 구현안해도 될 것 같음(?)
        Comment parentComment = commentRepository.findByIdAndActivatedTrue(parentCommentId).orElseThrow(NotFoundEntityException::new);
        // 대댓글을 달려했는데, 부모댓글이 삭제된 경우를 고려
        Comment childComment = toEntity(commentReqDto, post, member, parentComment);
        commentRepository.save(childComment);
        parentComment.addChildComment(childComment);
        commentRepository.save(parentComment);
        return BasicResDto.builder()
                .message("successful")
                .build();
    }

    public CommentListDto findCommentList(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(NotFoundEntityException::new);
        return new CommentListDto("successful", post);
    }

    public CommentListDto filterBlockMembersCommentList(CommentListDto commentListDto, Long postId, String kakaoId){
        Post post = postRepository.findById(postId).orElseThrow(NotFoundEntityException::new);

        // 이거 코드 성능이 굉장히 안좋아보이는데 개선이 필요한데 어떻게 해야지...?
        // JPA로 구현하는 방식으로 새로 생각해봐야할것 같기도...
        Member currentMember = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        List<Block> allBlocks = new ArrayList<>();
        Optional.ofNullable(currentMember.getBlockings()).ifPresent(allBlocks::addAll);
        Optional.ofNullable(currentMember.getBlockeds()).ifPresent(allBlocks::addAll);
        for(ParentCommentContent parentCommentContent : commentListDto.getParentCommentContents()){
            for(Block block : allBlocks){
                if (parentCommentContent.getCommentId().equals(block.getTarget().getId())){
                    parentCommentContent.setVisualized(false);
                    break;
                }
            }
            for (ChildCommentContent childCommentContent : parentCommentContent.getChildCommentList()){
                for (Block block : allBlocks){
                    if(childCommentContent.getMemberId().equals(block.getTarget().getId())){
                        parentCommentContent.getChildCommentList().remove(childCommentContent); // 자식 은 제거제거
                    }
                }
            }
        }

//        이 코드를 이용해서 리포지토리에서 blockedMembers로 찾아와서 서비스에서 여러과정에 걸쳐서 dto에 넣는 코드도 구상해볼 수 있을 것 같음
//        List<Member> blockedMembers = new ArrayList<>();
//        for (Block block : allBlocks){
//            blockedMembers.add(block.getTarget());
//        }

        return commentListDto;
    }
}


// 1. JPA 빼고 가져오는거야 member member select가 비효율,
//
// 2. 지금 한거 서비스는 하나의 비즈니스 검색 : 댓글검색 / 차단 댓글 필터링 -> X / dto변환
// 3.