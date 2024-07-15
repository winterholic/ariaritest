package youngpeople.aliali.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
import youngpeople.aliali.entity.member.Member;
import youngpeople.aliali.exception.common.NotFoundEntityException;
import youngpeople.aliali.manager.ImageManager;
import youngpeople.aliali.repository.ClubRepository;
import youngpeople.aliali.repository.ImageRepository;
import youngpeople.aliali.repository.MemberRepository;
import youngpeople.aliali.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
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
        if(postReqDto.getFixed()){
            List<Post> posts = postRepository.findByClubIdAndFixedAndPostType(clubId, true, postType);
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
        Post originPost = postRepository.findById(postId).orElseThrow(NotFoundEntityException::new);
        if (Boolean.FALSE.equals(originPost.getFixed()) && postReqDto.getFixed()){
            List<Post> posts = postRepository.findByClubIdAndFixedAndPostType(clubId, true, originPost.getPostType());
            if (posts.size() >= 3){
                return BasicResDto.builder()
                        .message("fail")
                        .build();
            }
        }

        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(NotFoundEntityException::new);

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

    public PostListDto findPostList(Long clubId, int pageIdx, PostType postType){
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageIdx - 1, pageSize);

        Page<Post> normalPage = postRepository.findByClubIdAndPostTypeAndFixedOrderByCreatedDateDesc(clubId, postType, false, pageRequest);
        List<Post> normalPosts = normalPage.hasContent() ? normalPage.getContent() : List.of();

        Page<Post> fixedPage = postRepository.findByClubIdAndPostTypeAndFixedOrderByCreatedDateDesc(clubId, postType, true, PageRequest.of(0, 5));
        List<Post> fixedPosts = fixedPage.hasContent() ? fixedPage.getContent() : List.of();

        return new PostListDto("successful", normalPosts, fixedPosts);
    }


}
