package youngpeople.aliali.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import youngpeople.aliali.controller.swagger.SwaggerAuth;
import youngpeople.aliali.dto.*;
import youngpeople.aliali.service.MemberService;

import java.io.IOException;

import static youngpeople.aliali.dto.MemberDto.*;
import static youngpeople.aliali.dto.SchoolDto.*;
import static youngpeople.aliali.controller.swagger.SwaggerExplain.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/my")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @SwaggerAuth
    @MemberGetMyExplain
    public MemberResDto getMy(HttpServletRequest request) {
        String kakaoId = getKakaoId(request);
        return memberService.findMy(kakaoId);
    }

    @PutMapping("/profile")
    @SwaggerAuth
    @MemberPutMyProfileExplain
    public BasicResDto updateMyProfile(HttpServletRequest request,
                                @RequestBody MemberUpdateProfileReqDto memberReqDto) {
        String kakaoId = getKakaoId(request);
        return memberService.updateMy(kakaoId, memberReqDto, null);
    }

    @PutMapping("/nickname")
    @SwaggerAuth
    @MemberPutMyNicknameExplain
    public BasicResDto updateMyNickname(HttpServletRequest request,
                                @RequestBody MemberUpdateNicknameReqDto memberReqDto) {
        String kakaoId = getKakaoId(request);
        return memberService.updateMy(kakaoId, null, memberReqDto);
    }

    @DeleteMapping
    @SwaggerAuth
    @MemberDeleteMyExplain
    public BasicResDto deleteMy(HttpServletRequest request) {
        String kakaoId = getKakaoId(request);
        return memberService.unregister(kakaoId);
    }

    @PostMapping("/auth/school-request")
    @SwaggerAuth
    @MemberAuthenticationSchoolRequestExplain
    public BasicResDto authenticationSchool(HttpServletRequest request,
                                            @RequestBody SchoolAuthReqDto schoolAuthReqDto) {
        String kakaoId = getKakaoId(request);
        return memberService.registerMySchool(kakaoId, schoolAuthReqDto);
    }

    @GetMapping("/auth/school-certification")
    @MemberAuthenticationSchoolCheckExplain
    public void schoolAuth(@RequestParam(name = "authToken") String authToken,
                                  HttpServletResponse response) throws IOException {
        memberService.authenticateSchool(authToken);
        response.sendRedirect("https://ariari.vercel.app");
    }

    private String getKakaoId(HttpServletRequest request) {
        return (String) request.getAttribute("kakaoId");
    }
}