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

    public BasicResDto SavePost(PostDto.PostReqDto postReqDto, Long clubId, String kakaoId, List<MultipartFile> imageFiles) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(NotFoundEntityException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(NotFoundEntityException::new);

        List<Image> images = new ArrayList<>();
        Post post = toEntity(postReqDto, club, member);

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
