package youngpeople.aliali.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import youngpeople.aliali.controller.swagger.SwaggerAuth;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.manager.ImageManager;
import youngpeople.aliali.service.RecruitmentService;

import static youngpeople.aliali.dto.RecruitmentDto.*;
import static youngpeople.aliali.controller.swagger.SwaggerExplain.*;

@Slf4j
@RestController
@RequestMapping("club/{clubId}/recruitment")
@RequiredArgsConstructor
public class RecruitmentController {

    private final RecruitmentService recruitmentService;
    private final ImageManager imageManager;

    /**
     * 비활성화
     */
//    @GetMapping("/list")
    @SwaggerAuth
    public RecruitmentsResDto getRecruitments(HttpServletRequest request,
                                              @PathVariable(name = "clubId") Long clubId) {
        String kakaoId = getKakaoId(request);
        return recruitmentService.findRecruitments(kakaoId, clubId);
    }

    /**
     * 수정된 부분
     */
    @GetMapping("/{recruitmentId}")
    @SwaggerAuth
    @RecruitmentGetDetailExplain
    public RecruitmentDetailResDto getRecruitment(HttpServletRequest request,
                               @PathVariable(name = "clubId") Long clubId,
                               @PathVariable(name = "recruitmentId") Long recruitmentId) {
        String kakaoId = getKakaoId(request);
        return recruitmentService.findRecruitment(kakaoId, clubId, recruitmentId);
    }

    /**
     * image 처리 수정
     */
    @PostMapping
    @SwaggerAuth
    @RecruitmentRegisterExplain
    public BasicResDto registerRecruitment(HttpServletRequest request,
                                           @Parameter(description = "첨부파일") @RequestPart(name = "profile") MultipartFile profile,
                                           @PathVariable(name = "clubId") Long clubId,
                                           @RequestBody RecruitmentReqDto recruitmentReqDto) {
        String profileUrl = imageManager.imageSave(profile);
        String kakaoId = getKakaoId(request);

        recruitmentService.registerRecruitment(kakaoId, clubId, recruitmentReqDto, profileUrl);

        return BasicResDto.builder()
                .message("successful").build();
    }

    /**
     * image 처리 수정
     */
    @PutMapping("/{recruitmentId}")
    @SwaggerAuth
    @RecruitmentRegisterModifyExplain
    public BasicResDto updateRecruitment(HttpServletRequest request,
                                         @Parameter(description = "첨부파일") @RequestPart(name = "profile") MultipartFile profile,
                                         @PathVariable(name = "clubId") Long clubId,
                                         @PathVariable(name = "recruitmentId") Long recruitmentId,
                                         @RequestBody RecruitmentReqDto recruitmentReqDto) {
        String profileUrl = imageManager.imageSave(profile);
        String kakaoId = getKakaoId(request);
        return recruitmentService.updateRecruitment(kakaoId, clubId, recruitmentId, recruitmentReqDto, profileUrl);
    }

    @DeleteMapping("/{recruitmentId}")
    @SwaggerAuth
    @RecruitmentDeleteExplain
    public BasicResDto deleteRecruitment(HttpServletRequest request,
                                  @PathVariable(name = "clubId") Long clubId,
                                  @PathVariable(name = "recruitmentId") Long recruitmentId) {
        String kakaoId = getKakaoId(request);
        return recruitmentService.deleteRecruitment(kakaoId, clubId, recruitmentId);
    }

    private String getKakaoId(HttpServletRequest request) {
        return (String) request.getAttribute("kakaoId");
    }

}
