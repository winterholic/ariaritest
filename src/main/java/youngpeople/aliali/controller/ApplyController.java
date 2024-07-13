package youngpeople.aliali.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import youngpeople.aliali.controller.swagger.SwaggerAuth;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.service.ApplyService;

import static youngpeople.aliali.dto.ApplyDto.*;
import static youngpeople.aliali.controller.swagger.SwaggerExplain.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ApplyController {

    private final ApplyService applyService;

    @GetMapping("/club/{clubId}/recruitment/{recruitmentId}/apply/list")
    @SwaggerAuth
    @ApplyClubListExplain
    public AppliesResDto getApplies(HttpServletRequest request,
                                    @PathVariable(name = "recruitmentId") Long recruitmentId) {
        String kakaoId = getKakaoId(request);
        return applyService.findApplies(kakaoId, recruitmentId);
    }

    @GetMapping("/club/{clubId}/recruitment/{recruitmentId}/apply/{applyId}")
    @SwaggerAuth
    @ApplyClubDetailExplain
    public ApplyDetailResDto getApply(HttpServletRequest request,
                                      @PathVariable(name = "applyId") Long applyId) {
        String kakaoId = getKakaoId(request);
        return applyService.findApply(kakaoId, applyId);
    }

    @PostMapping("/club/{clubId}/recruitment/{recruitmentId}/apply")
    @SwaggerAuth
    @ApplyRegisterExplain
    public BasicResDto registerApply(HttpServletRequest request,
                                     @PathVariable(name = "recruitmentId") Long recruitmentId,
                                     @RequestBody ApplyReqDto applyReqDto) {
        String kakaoId = getKakaoId(request);
        applyService.registerApply(kakaoId, recruitmentId, applyReqDto);
        return BasicResDto.builder()
                .message("successful").build();
    }

    @DeleteMapping("/club/{clubId}/recruitment/{recruitmentId}/apply/{applyId}")
    @SwaggerAuth
    @ApplyDeleteExplain
    public BasicResDto deleteApply(HttpServletRequest request,
                                      @PathVariable(name = "applyId") Long applyId) {
        String kakaoId = getKakaoId(request);
        return applyService.deleteApply(kakaoId, applyId);
    }

    /******************************
     *
     * 의도 파악 후 수정
     */
    @PostMapping("/club/{clubId}/recruitment/{recruitmentId}/apply/judge")
    @SwaggerAuth
    @ApplyClubApproveListExplain
    public BasicResDto judgeApplies(HttpServletRequest request,
                                     @PathVariable(name = "clubId") Long clubId,
                                     @RequestBody ApplyProcessingDto applyProcessingDto){
        String kakaoId = getKakaoId(request);
        applyService.judgeApplies(kakaoId, clubId, applyProcessingDto);

        return BasicResDto.builder()
                .message("successful").build();
    }

    @GetMapping("/my/apply/list")
    @SwaggerAuth
    @ApplyMyListExplain
    public MyAppliesDto getMyApplies(HttpServletRequest request){
        String kakaoId = getKakaoId(request);
        return applyService.findMyApplies(kakaoId);
    }

    @PostMapping("/my/apply/{applyId}/approve")
    @SwaggerAuth
    @ApplyMyApproveExplain
    public BasicResDto approveIndividual(HttpServletRequest request,
                                         @PathVariable(name = "applyId") Long applyId) {
        String kakaoId = getKakaoId(request);
        applyService.approveMyApply(kakaoId, applyId);

        return BasicResDto.builder()
                .message("successful").build();
    }

    @PostMapping("/my/apply/{applyId}/refuse")
    @SwaggerAuth
    @ApplyMyRefuseExplain
    public BasicResDto refuseIndividual(HttpServletRequest request,
                                        @PathVariable(name = "applyId") Long applyId) {
        String kakaoId = getKakaoId(request);
        applyService.refuseMyApply(kakaoId, applyId);

        return BasicResDto.builder()
                .message("successful").build();
    }

    private String getKakaoId(HttpServletRequest request) {
        return (String) request.getAttribute("kakaoId");
    }
}