package youngpeople.aliali.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.entity.Image;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.club.Post;
import youngpeople.aliali.entity.clubmember.ClubMember;
import youngpeople.aliali.entity.enumerated.ImageTargetType;
import youngpeople.aliali.entity.enumerated.PostType;
import youngpeople.aliali.entity.member.Block;
import youngpeople.aliali.entity.member.Member;
import youngpeople.aliali.exception.clubmember.NotExistingInClubException;
import youngpeople.aliali.exception.common.NotFoundEntityException;
import youngpeople.aliali.exception.block.BlockedMemberAccessException;
import youngpeople.aliali.exception.post.FixedCountException;
import youngpeople.aliali.exception.post.ModifyingAutorityException;
import youngpeople.aliali.manager.ImageManager;
import youngpeople.aliali.repository.*;

import java.util.*;
import java.util.stream.Collectors;

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
    private final ClubMemberRepository clubMemberRepository;

    public Boolean isFixable(Long clubId) {
        //return (postRepository.findByClubIdAndFixedAndPostType(clubId, true, PostType.NOTICE).size() < 3);
        return postRepository.countByClubIdAndFixedAndPostType(clubId, true, PostType.NOTICE) < 3; // 고정개수 3개 이하인지 검사
    }

    public void checkFixablePostAtSaving(PostReqDto postReqDto, Long clubId) { // 게시글 작성 시 예외처리
        if (postReqDto.getFixed() && !isFixable(clubId)) {
            throw new FixedCountException();
        }
    }

    public void checkFixablePostAtModifyingPost(PostReqDto postReqDto, Long clubId, Post originPost){ // 게시글 수정 시 예외처리
        if(postReqDto.getFixed() && !originPost.getFixed() && !isFixable(clubId)){
            throw new FixedCountException();
        }
    }

    public void checkFixablePostAtModifyingFixed(Long clubId){
        if(!isFixable(clubId)){
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

    public void checkMemberOfAuthorityAtPost(Long memberId, Long clubId){
        clubMemberRepository.findByMemberIdAndClubId(memberId, clubId).orElseThrow(NotExistingInClubException::new);
    }

    public void checkOwnerOfPost(Member targetMember, Post post){
        if(!(targetMember.equals(post.getMember()))){
            throw new ModifyingAutorityException();
        }
    }

    public void checkAdminAuthorityOfPost(Member targetMember, Post post){
        clubMemberRepository.findByMemberAndClub(targetMember, post.getClub()).orElseThrow(ModifyingAutorityException::new);
    }


    public BasicResDto savePost(PostReqDto postReqDto, Long clubId, PostType postType, String kakaoId, List<MultipartFile> imageFiles) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(NotFoundEntityException::new);
        checkMemberOfAuthorityAtPost(member.getId(), clubId);
        checkFixablePostAtSaving(postReqDto, clubId);

        Post post = toEntity(postReqDto, club, member, postType);

        List<Image> images = saveImageWithPost(imageFiles, post);
        post.setImages(images);
        postRepository.save(post);

        return BasicResDto.builder()
                .message("successful")
                .build();
    }

    public BasicResDto modifyPost(PostReqDto postReqDto, Long clubId, Long postId, String kakaoId, List<MultipartFile> imageFiles){
        Post originPost = postRepository.findById(postId).orElseThrow(NotFoundEntityException::new);
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);

        checkOwnerOfPost(member, originPost);
        checkFixablePostAtModifyingPost(postReqDto, clubId, originPost);

        softDeleteImageData(originPost); // 이미지가 변경 되었는지 알 수 있는 데이터를 함께 받는다면, 좀 더 정밀한 코드개선이 필요

        List<Image> images = saveImageWithPost(imageFiles, originPost);
        updateEntity(postReqDto, originPost, images);

        return BasicResDto.builder()
                .message("successful")
                .build();
    }

    public BasicResDto modifyFixedPost(Long postId, Long clubId, String kakaoId, boolean fixed){
        Post originPost = postRepository.findById(postId).orElseThrow(NotFoundEntityException::new);
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);

        checkAdminAuthorityOfPost(member, originPost); // 관리자권한이면 수정가능

        if(originPost.getFixed() && !fixed){
            originPost.setFixed(false);
        }
        else if(!originPost.getFixed() && fixed){
            checkFixablePostAtModifyingFixed(clubId);
            originPost.setFixed(true);
        }
        return BasicResDto.builder()
                .message("successful")
                .build();
    }

    public BasicResDto deletePost(Long postId, String kakaoId){
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Post post = postRepository.findById(postId).orElseThrow(NotFoundEntityException::new);
        checkOwnerOfPost(member, post);
        softDeleteImageData(post);
        post.setActivated(false);
        postRepository.save(post); // 필요?
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

    public NoticePostListDto findNoticePostList(Long clubId, int pageIdx, String kakaoId){
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        checkMemberOfAuthorityAtPost(member.getId(), clubId);

        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageIdx - 1, pageSize);

        Page<Post> normalPage = postRepository.findByClubIdAndPostTypeAndFixedOrderByCreatedDateDesc(clubId, PostType.NOTICE, false, pageRequest);
        int totalPages = normalPage.getTotalPages();
        List<Post> normalPosts = normalPage.hasContent() ? normalPage.getContent() : List.of();


        Page<Post> fixedPage = postRepository.findByClubIdAndPostTypeAndFixedOrderByCreatedDateDesc(clubId, PostType.NOTICE, true, PageRequest.of(0, 3));
        List<Post> fixedPosts = fixedPage.hasContent() ? fixedPage.getContent() : List.of();

        return new NoticePostListDto("successful", normalPosts, fixedPosts, totalPages);
    }

    public ImageListDto imageList(Long clubId, int pageIdx, int pageSize){
        //이미지 리스트는 클럽의 비회원도 가능
        PageRequest pageRequest = PageRequest.of(pageIdx, pageSize);
        List<Image> images = imageRepository.findByClubIdOrderByCreatedDateDesc(clubId, pageRequest);
        return new ImageListDto("successful", images);
    }

    public GeneralPostListDto findGeneralPostList(String kakaoId, Long clubId, int pageIdx){
        Member currentMember = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        checkMemberOfAuthorityAtPost(currentMember.getId(), clubId);
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageIdx - 1, pageSize);
        Page<Post> page = postRepository.findByMemberNotInAndClubIdAndPostTypeOrderByCreatedDateDesc(findBlockMember(currentMember), clubId, PostType.GENERAL, pageRequest);
        int totalPages = page.getTotalPages();
        List<Post> posts = page.hasContent() ? page.getContent() : List.of();
        return new GeneralPostListDto("successful", posts, totalPages);
    }

    public PostDetailDto findGeneralPostDetail(String kakaoId, Long postId, Long clubId){
        Member currentMember = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        checkMemberOfAuthorityAtPost(currentMember.getId(), clubId);
        Post post = postRepository.findById(postId).orElseThrow(NotFoundEntityException::new);
        Member postAuthor = memberRepository.findByKakaoId(post.getMember().getKakaoId()).orElseThrow(NotFoundEntityException::new);
        checkAbleToFindPost(currentMember, postAuthor);
        return new PostDetailDto("successful", post);
    }

    public PostDetailDto findNoticePostDetail(String kakaoId, Long postId, Long clubId){
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        checkMemberOfAuthorityAtPost(member.getId(), clubId);
        Post post = postRepository.findById(postId).orElseThrow(NotFoundEntityException::new);
        return new PostDetailDto("successful", post);
    }
}
