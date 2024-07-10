package youngpeople.aliali.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import youngpeople.aliali.controller.swagger.SwaggerAuth;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.service.ClubMemberService;

import static youngpeople.aliali.dto.ClubMemberDto.*;
import static youngpeople.aliali.controller.swagger.SwaggerExplain.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ClubMemberController {

    private final ClubMemberService clubMemberService;

    @GetMapping("/club/{clubId}/club-members")
    @SwaggerAuth
    @ClubMemberGetExplain
    public ClubMembersResDto getClubMembers(HttpServletRequest request,
                                            @PathVariable(name = "clubId") Long clubId) {
        String kakaoId = getKakaoId(request);
        return clubMemberService.findClubMembers(kakaoId, clubId);
    }

    @PutMapping("/club/{clubId}/club-members")
    @SwaggerAuth
    @ClubMemberPutExplain
    public BasicResDto updateClubMembers(HttpServletRequest request,
                                         @PathVariable(name = "clubId") Long clubId,
                                         @RequestBody ClubMembersUpdateReqDto clubMembersUpdateReqDto) {
        String kakaoId = getKakaoId(request);
        return clubMemberService.updateClubMembers(kakaoId, clubId, clubMembersUpdateReqDto);
    }

    @PostMapping("/club/{clubId}/club-members")
    @SwaggerAuth
    @ClubMemberDeleteExplain
    public BasicResDto deleteClubMembers(HttpServletRequest request,
                                         @PathVariable(name = "clubId") Long clubId,
                                         @RequestBody ClubMembersDeleteReqDto clubMembersDeleteReqDto) {
        String kakaoId = getKakaoId(request);
        return clubMemberService.deleteClubMembers(kakaoId, clubId, clubMembersDeleteReqDto);
    }

    @DeleteMapping("/club/{clubId}/club-members")
    @SwaggerAuth
    @ClubMemberDeleteMyExplain
    public BasicResDto unregister(HttpServletRequest request,
                                  @PathVariable(name = "clubId") Long clubId) {
        String kakaoId = getKakaoId(request);
        return clubMemberService.unregister(kakaoId, clubId);
    }


    private String getKakaoId(HttpServletRequest request) {
        return (String) request.getAttribute("kakaoId");
    }

}
