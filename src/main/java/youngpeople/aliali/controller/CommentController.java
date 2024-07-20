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

    @GetMapping("/notice/{postId}/comment")
    @SwaggerAuth
    @NoticeCommentListExplain
    public NoticeCommentListDto noticeCommentList(HttpServletRequest request, @PathVariable("clubId") Long clubId, @PathVariable("postId") Long postId){
        String kakaoId = getKakaoId(request);
        return commentService.findNoticeCommentList(postId, kakaoId, clubId);
    }

    @GetMapping("/general/{postId}/comment")
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


//해야하는 일 정리
//delete구현 post + comment
//1개 post 고정요청 구현
//club멤버 아닌데 게시글 게시판 조회나 상세조회 막기
//예외처리할 경우의수가 많은듯 기능이 많아서... 예외처리하기
// 무슨오류가 있는지 모르겠는데 실행 안되네 인생망했네 별거아니었네 또 대소문자야?