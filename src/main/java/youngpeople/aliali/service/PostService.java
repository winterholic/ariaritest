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
import youngpeople.aliali.entity.club.Post;
import youngpeople.aliali.entity.enumerated.ImageTargetType;
import youngpeople.aliali.entity.enumerated.PostType;
import youngpeople.aliali.entity.member.Block;
import youngpeople.aliali.entity.member.Member;
import youngpeople.aliali.exception.common.NotFoundEntityException;
import youngpeople.aliali.manager.ImageManager;
import youngpeople.aliali.repository.ClubRepository;
import youngpeople.aliali.repository.ImageRepository;
import youngpeople.aliali.repository.MemberRepository;
import youngpeople.aliali.repository.PostRepository;

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

    public BasicResDto savePost(PostReqDto postReqDto, Long clubId, PostType postType, String kakaoId, List<MultipartFile> imageFiles) {
        // 고정된 post의 개수가 3개 이상일 때 막는 역할 코드 개선이 필요해보임
        // 공지일경우, 그리고 일반게시글인 경우로 나눠서 로직변경
        // 공지만 상단 고정 가능하게 변경, 일반게시글을 고정할 때에 차단로직에 관한 논리가 꼬인다.
        if(postReqDto.getFixed()){
            if (postType.equals(PostType.GENERAL)){
                return BasicResDto.builder()
                        .message("fail")
                        .build();
            }
            List<Post> posts = postRepository.findByClubIdAndFixedAndPostType(clubId, true, PostType.GENERAL);
            if (posts.size() >= 3){
                return BasicResDto.builder()
                        .message("fail")
                        .build();
            }
        }
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(NotFoundEntityException::new);

        List<Image> images = new ArrayList<>();
        Post post = toEntity(postReqDto, club, member, postType);

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

        post.setImages(images);
        postRepository.save(post);

        return BasicResDto.builder()
                .message("successful")
                .build();
    }

    public BasicResDto modifyPost(PostReqDto postReqDto, Long clubId, Long postId, String kakaoId, List<MultipartFile> imageFiles){
        // 우리가 posttype을 인자로 안받는 경우로 그대로 구현하는 경우에 오류로직 구현은 프론트엔드에서 애초에 처리되어야할듯
        Post originPost = postRepository.findById(postId).orElseThrow(NotFoundEntityException::new);
        if (Boolean.FALSE.equals(originPost.getFixed()) && postReqDto.getFixed()){
            List<Post> posts = postRepository.findByClubIdAndFixedAndPostType(clubId, true, originPost.getPostType());
            if (posts.size() >= 3){
                return BasicResDto.builder()
                        .message("fail")
                        .build();
            }
        }

        List<Image> images = new ArrayList<>();
        //기존이미지 삭제로직필요

        // 데이터 삽입과 공통코드 개선 필요
        for (MultipartFile imageFile : imageFiles) {
            Image image = null;
            if (imageFile != null) {
                String imageUrl = imageManager.imageSave(imageFile);
                image = new Image(ImageTargetType.POST, imageUrl);
            }
            images.add(image);
            if (image != null) {
                image.setPost(originPost);
                imageRepository.save(image);
            }
        }

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
                return new PostDetailDto("fail", post); // fail부분들 논의 필요 // 이거 컨트롤러에서 해줘야할거같은데...
            }
        }
        return postDetailDto;
    }

    public BasicResDto saveParentComment(){
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
        return commentListDto;
    }
}
