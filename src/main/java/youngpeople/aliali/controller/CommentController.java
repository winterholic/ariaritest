package youngpeople.aliali.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import youngpeople.aliali.controller.swagger.SwaggerAuth;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.dto.PostDto;
import youngpeople.aliali.service.CommentService;

import static youngpeople.aliali.dto.CommentDto.*;
import static youngpeople.aliali.controller.swagger.SwaggerExplain.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/club/{clubId}") // clubId가 필요함 예외처리시에 필요
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}/comment")
    @SwaggerAuth
    @WriteParentCommentExplain
    public BasicResDto parentCommentAdd(HttpServletRequest request, @PathVariable("postId") Long postId,
                                        @PathVariable("clubId") Long clubId, @RequestBody CommentReqDto commentReqDto){
        String kakaoId = getKakaoId(request);
        return commentService.saveParentComment(commentReqDto, kakaoId, postId, clubId);
    }

    @PostMapping("{postId}/{commentId}/comment")
    @SwaggerAuth
    @WriteChildCommentExplain
    public BasicResDto childCommentAdd(HttpServletRequest request, @PathVariable("clubId") Long clubId, @PathVariable("postId") Long postId,
                                       @PathVariable("commentId") Long commentId, @RequestBody CommentReqDto commentReqDto){
        String kakaoId = getKakaoId(request);
        return commentService.saveChildComment(commentReqDto, kakaoId, postId, commentId, clubId);
    }

    @DeleteMapping("{commentId}")
    @SwaggerAuth
    @DeleteCommentExplain
    public BasicResDto deleteComment(HttpServletRequest request, @PathVariable("commentId") Long commentId){
        String kakaoId = getKakaoId(request);
        return commentService.DeleteComment(kakaoId, commentId);
    }

    @GetMapping("/notice/{postId}/comments")
    @SwaggerAuth
    @NoticeCommentListExplain
    public NoticeCommentListDto noticeCommentList(HttpServletRequest request, @PathVariable("clubId") Long clubId, @PathVariable("postId") Long postId){
        String kakaoId = getKakaoId(request);
        return commentService.findNoticeCommentList(postId, kakaoId, clubId);
    }

    @GetMapping("/general/{postId}/comments")
    @SwaggerAuth
    @GeneralCommentListExplain
    public GeneralCommentListDto generalCommentList(HttpServletRequest request, @PathVariable("clubId") Long clubId, @PathVariable("postId") Long postId){
        String kakaoId = getKakaoId(request);
        return commentService.findGeneralCommentList(postId, kakaoId, clubId);
    }

    private String getKakaoId(HttpServletRequest request) {
        return (String) request.getAttribute("kakaoId");
    }
}

