package youngpeople.aliali.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import youngpeople.aliali.controller.swagger.SwaggerAuth;
import youngpeople.aliali.controller.swagger.SwaggerExplain;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.dto.PostDto;
import youngpeople.aliali.entity.enumerated.PostType;
import youngpeople.aliali.service.PostService;

import java.util.List;

import static youngpeople.aliali.dto.PostDto.*;
import static youngpeople.aliali.controller.swagger.SwaggerExplain.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/club/{clubId}/post")
public class PostController {
    private final PostService postService;

    @PostMapping(value = "/{postType}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @SwaggerAuth
    @WritePostExplain
    public BasicResDto postAdd(HttpServletRequest request,
                               @PathVariable("clubId") Long clubId,
                               @PathVariable("postType") PostType postType,
                               @Parameter(description = "imageFiles") @RequestPart(name = "imageFiles") List<MultipartFile> imageFiles,
                               @Parameter(description = "postReqDto") @RequestPart(name = "postReqDto") PostDto.PostReqDto postReqDto){
        String kakaoId = getKakaoId(request);
        return postService.savePost(postReqDto, clubId, postType, kakaoId, imageFiles);
    }

    @PutMapping(value = "/{postId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @SwaggerAuth
    @ModifyPostExplain
    public BasicResDto postModify(HttpServletRequest request,
                               @PathVariable("clubId") Long clubId,
                               @PathVariable("postId") Long postId,
                               @Parameter(description = "imageFiles") @RequestPart(name = "imageFiles") List<MultipartFile> imageFiles,
                               @Parameter(description = "postReqDto") @RequestPart(name = "postReqDto") PostDto.PostReqDto postReqDto){
        String kakaoId = getKakaoId(request);
        return postService.modifyPost(postReqDto, clubId, postId, kakaoId, imageFiles);
    }

    @PatchMapping(value = "{postId}") //패치 이런식으로 하는거 맞나요?
    @SwaggerAuth
    @ModifyPostFixedExplain
    public BasicResDto postPatch(HttpServletRequest request, @PathVariable("clubId") Long clubId, @PathVariable("postId") Long postId,
        @RequestBody FixedReqDto fixedReqDto){
        String kakaoId = getKakaoId(request);
        return postService.modifyFixedPost(postId, clubId, kakaoId, fixedReqDto.isFixed());
    }

    @DeleteMapping("{postId}")
    @SwaggerAuth
    @DeletePostExplain
    public BasicResDto postDelete(HttpServletRequest request, @PathVariable("clubId") Long clubId, @PathVariable Long postId){
        String kakaoId = getKakaoId(request);
        return postService.deletePost(postId, kakaoId);
    }

    @GetMapping("/list/thumbnail")
    @ThumnailPostsExplain
    public MainPagePostListDto MainPagePostList(@PathVariable("clubId") Long clubId){
        return postService.mainClubPagePostList(clubId);
    }

    @GetMapping("/images/thumbnail")
    @ThumnailImagesExplain
    public ImageListDto MainPageImageList(@PathVariable("clubId") Long clubId){
        return postService.imageList(clubId, 0, 5);
    }

    @GetMapping("/image/list/{pageIdx}")
    @ImageListExplain
    public ImageListDto allImageList(@PathVariable("clubId") Long clubId, @PathVariable("pageIdx") int pageIdx){
        return postService.imageList(clubId, pageIdx, 25);
    }

    @GetMapping("/notice/list/{pageIdx}")
    @SwaggerAuth
    @NoticePostListExplain
    public NoticePostListDto noticePostList(HttpServletRequest request, @PathVariable("clubId") Long clubId, @PathVariable("pageIdx") int pageIdx){
        String kakaoId = getKakaoId(request);
        return postService.findNoticePostList(clubId, pageIdx, kakaoId);
    }

    @GetMapping("/general/list/{pageIdx}")
    @SwaggerAuth
    @GeneralPostListExplain
    public GeneralPostListDto generalPostList(HttpServletRequest request, @PathVariable("clubId") Long clubId, @PathVariable("pageIdx") int pageIdx){
        String kakaoId = getKakaoId(request);
        return postService.findGeneralPostList(kakaoId, clubId, pageIdx);
    }

    @GetMapping("/general/{postId}")
    @SwaggerAuth
    @GeneralPostDetailExplain
    public PostDetailDto generalPostDetail(HttpServletRequest request, @PathVariable("clubId") Long clubId, @PathVariable("postId") Long postId){
        String kakaoId = getKakaoId(request);
        return postService.findGeneralPostDetail(kakaoId, postId, clubId);
    }

    @GetMapping("/notice/{postId}")
    @SwaggerAuth
    @NoticePostDetailExplain
    public PostDetailDto NoticePostDetail(HttpServletRequest request, @PathVariable("clubId") Long clubId, @PathVariable("postId") Long postId){
        String kakaoId = getKakaoId(request);
        return postService.findNoticePostDetail(kakaoId, postId, clubId);
    }

    private String getKakaoId(HttpServletRequest request) {
        return (String) request.getAttribute("kakaoId");
    }
}
