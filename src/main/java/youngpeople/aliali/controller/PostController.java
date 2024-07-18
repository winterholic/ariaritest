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
        return postService.modifyPost(postReqDto, clubId, postId, imageFiles);
    }

//    @DeleteMapping("{postId}")
//    @SwaggerAuth
//    public BasicResDto postDelete(HttpServletRequest request, @PathVariable("clubId") Long clubId, @PathVariable Long postId){
//        String kakaoId = getKakaoId(request);
//        return new BasicResDto("success");
//    }

    @GetMapping("/list/thumbnail")
    public MainPagePostListDto MainPagePostList(@PathVariable("clubId") Long clubId){
        return postService.mainClubPagePostList(clubId);
    }

    @GetMapping("/image/thumbnail")
    public ImageListDto MainPageImageList(@PathVariable("clubId") Long clubId){
        return postService.mainClubPageImageList(clubId);
    }

    @GetMapping("/image/list/{pageIdx}")
    public ImageListDto allImageList(@PathVariable("clubId") Long clubId, @PathVariable("pageIdx") int pageIdx){
        return postService.findImageList(clubId, pageIdx);
    } // 기획따라 달라질 것 같은데, 일단 전체를 한번에 로딩하면 유저입장에서 느릴테니 따로 구현했음

    @GetMapping("/notice/list/{pageIdx}")
    @SwaggerAuth
    public NoticePostListDto noticePostList(@PathVariable("clubId") Long clubId, @PathVariable("pageIdx") int pageIdx){
        return postService.findNoticePostList(clubId, pageIdx);
    }

    @GetMapping("/general/list/{pageIdx}")
    @SwaggerAuth
    public GeneralPostListDto generalPostList(HttpServletRequest request, @PathVariable("clubId") Long clubId, @PathVariable("pageIdx") int pageIdx){
        String kakaoId = getKakaoId(request);
        return postService.findGeneralPostList(kakaoId, clubId, pageIdx);
    }

    //clubId를 안쓰는 친구들이 많은데 공통 매핑 빼버릴까?
    @GetMapping("/{postId}")
    @SwaggerAuth
    public PostDetailDto postDetail(HttpServletRequest request, @PathVariable("clubId") Long clubId, @PathVariable("postId") Long postId){
        String kakaoId = getKakaoId(request);
        return postService.filterBlockMembersDetailPost(postService.findDetailPost(postId), kakaoId, postId);
    }

    @PostMapping("/{postId}/comment")
    @SwaggerAuth
    public BasicResDto parentCommentAdd(HttpServletRequest request, @PathVariable("postId") Long postId,
                                        @PathVariable("clubId") Long clubId, @RequestBody CommentReqDto commentReqDto){
        String kakaoId = getKakaoId(request);
        return postService.saveParentComment(commentReqDto, kakaoId, postId);
    }

    @PostMapping("{postId}/{commentId}/comment")
    @SwaggerAuth
    public BasicResDto childCommentAdd(HttpServletRequest request, @PathVariable("clubId") Long clubId, @PathVariable("postId") Long postId,
                                       @PathVariable("commentId") Long commentId, @RequestBody CommentReqDto commentReqDto){
        String kakaoId = getKakaoId(request);
        return postService.saveChildComment(commentReqDto, kakaoId, postId, commentId);
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
