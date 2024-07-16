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
    //@WritePostExplain
    public BasicResDto postModify(HttpServletRequest request,
                               @PathVariable("clubId") Long clubId,
                               @PathVariable("postId") Long postId,
                               @Parameter(description = "imageFiles") @RequestPart(name = "imageFiles") List<MultipartFile> imageFiles,
                               @Parameter(description = "postReqDto") @RequestPart(name = "postReqDto") PostDto.PostReqDto postReqDto){
        String kakaoId = getKakaoId(request);
        return postService.modifyPost(postReqDto, clubId, postId, kakaoId, imageFiles);
    }

    @GetMapping("/{postType}/list/{pageIdx}")
    public PostListDto generalPostList(@PathVariable("clubId") Long clubId, @PathVariable("postType") PostType postType, @PathVariable("pageIdx") int pageIdx){
        return postService.findPostList(clubId, pageIdx, postType);
    }

//    @GetMapping("/images")
//    public ImageListDto

    @GetMapping("/{postId}")
    @SwaggerAuth
    public PostDetailDto postDetails(HttpServletRequest request, @PathVariable("clubId") Long clubId, @PathVariable("postId") Long postId){
        String kakaoId = getKakaoId(request);
        return postService.filterBlockMembersDetailPost(postService.findDetailPost(postId), kakaoId, postId);
    }

    @GetMapping("/{postId}/comment")
    @SwaggerAuth
    public CommentListDto allCommentList(HttpServletRequest request, @PathVariable("clubId") Long clubId, @PathVariable("postId") Long postId){
        String kakaoId = getKakaoId(request);
        return postService.filterBlockMembersCommentList(postService.findCommentList(postId), clubId, kakaoId);
    }

    private String getKakaoId(HttpServletRequest request) {
        return (String) request.getAttribute("kakaoId");
    }
}
