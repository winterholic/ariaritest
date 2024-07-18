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
import youngpeople.aliali.exception.post.FixedCountException;
import youngpeople.aliali.manager.ImageManager;
import youngpeople.aliali.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        //기존이미지 삭제로직필요

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

    // 관리자는 차단 불가능하게 구현해야할듯
    // 차단여부 상관없이 관리자 공지사항
    public NoticePostListDto findNoticePostList(Long clubId, int pageIdx){
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageIdx - 1, pageSize);

        Page<Post> normalPage = postRepository.findByClubIdAndPostTypeAndFixedOrderByCreatedDateDesc(clubId, PostType.NOTICE, false, pageRequest);
        List<Post> normalPosts = normalPage.hasContent() ? normalPage.getContent() : List.of();

        Page<Post> fixedPage = postRepository.findByClubIdAndPostTypeAndFixedOrderByCreatedDateDesc(clubId, PostType.NOTICE, true, PageRequest.of(0, 3));
        List<Post> fixedPosts = fixedPage.hasContent() ? fixedPage.getContent() : List.of();

        return new NoticePostListDto("successful", normalPosts, fixedPosts);
    }

    public ImageListDto mainClubPageImageList(Long clubId){
        PageRequest pageRequest = PageRequest.of(0, 5); // 기획(디자인) 따라서 수정가능한 부분
        List<Image> images = imageRepository.findByClubIdOrderByCreatedDateDesc(clubId, pageRequest);
        return new ImageListDto("successful", images);
    }

    public ImageListDto findImageList(Long clubId, int pageIdx){
        PageRequest pageRequest = PageRequest.of(pageIdx, 25); // 기획(디자인) 따라서 수정 가능한 부분
        List<Image> images = imageRepository.findByClubIdOrderByCreatedDateDesc(clubId, pageRequest);
        return new ImageListDto("successful", images);
    }

    public GeneralPostListDto findGeneralPostList(String kakaoId, Long clubId, int pageIdx){
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageIdx - 1, pageSize);
        Member currentMember = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        List<Block> allBlocks = new ArrayList<>();
        Optional.ofNullable(currentMember.getBlockings()).ifPresent(allBlocks::addAll);
        Optional.ofNullable(currentMember.getBlockeds()).ifPresent(allBlocks::addAll);
        List<Member> members = new ArrayList<>();
        for(Block block : allBlocks){
            members.add(block.getTarget());
        }
        Page<Post> page = postRepository.findByMemberNotInAndClubIdAndPostTypeOrderByCreatedDateDesc(members, clubId, PostType.GENERAL, pageRequest);
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
        Member postAuthorMember = memberRepository.findByKakaoId(post.getMember().getKakaoId()).orElseThrow(NotFoundEntityException::new);
        Member currentMember = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
//        List<Block> blockings = postAuthorMember.getBlockings();
//        List<Block> blockeds = postAuthorMember.getBlockeds();
//        List<Block> allBlocks = new ArrayList<>();
//        if (blockings != null) {allBlocks.addAll(blockings);}
//        if (blockeds != null) {allBlocks.addAll(blockeds);}
        List<Block> allBlocks = new ArrayList<>();
        Optional.ofNullable(postAuthorMember.getBlockings()).ifPresent(allBlocks::addAll);
        Optional.ofNullable(postAuthorMember.getBlockeds()).ifPresent(allBlocks::addAll);
        for (Block block : allBlocks) {
            if (currentMember.equals(block.getTarget())){
                return new PostDetailDto("fail", post); // fail부분들 수정해야함 // 근데 이거 기능이 너무 많아져서 이거 컨트롤러에서 해줘야할거같은데...
            }
        }
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