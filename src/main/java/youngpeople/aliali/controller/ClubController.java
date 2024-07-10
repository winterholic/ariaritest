package youngpeople.aliali.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import youngpeople.aliali.controller.swagger.SwaggerAuth;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.service.ClubService;
import youngpeople.aliali.manager.ImageManager;

import static youngpeople.aliali.dto.ClubDto.*;
import static youngpeople.aliali.controller.swagger.SwaggerExplain.*;

@Slf4j
@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @GetMapping("/detail/{clubId}")
    @ClubGetDetailExplain
    public ClubDetailResDto getClub(@PathVariable(name = "clubId") Long clubId) {
        return clubService.findClub(clubId);
    }

    @GetMapping("/list/my")
    @SwaggerAuth
    @ClubGetMyExplain
    public ClubsResDto getMyClubs(HttpServletRequest request) {
        String kakaoId = getKakaoId(request);
        return clubService.findMyClubs(kakaoId);
    }

    @GetMapping("/list/bookmark")
    @SwaggerAuth
    @ClubGetBookmarkExplain
    public ClubsResDto getBookmarkClubs(HttpServletRequest request) {
        String kakaoId = getKakaoId(request);
        return clubService.findBookmarkClubs(kakaoId);
    }

    @PostMapping
    @SwaggerAuth
    @ClubRegisterExplain
    public BasicResDto registerClub(HttpServletRequest request,
                                    @RequestBody ClubReqDto clubReqDto,
                                    @RequestParam("image") MultipartFile imageFile) {
        String kakaoId = getKakaoId(request);
        return clubService.registerClub(clubReqDto, kakaoId, imageFile);
    }

    /**
     * 이미지 데이터 저장 추가 해야하져? -> 일단 했습니다.
     */
    @PutMapping("/{clubId}")
    @SwaggerAuth
    @ClubPutExplain
    public BasicResDto updateClub(HttpServletRequest request,
                                 @PathVariable(name = "clubId") Long clubId,
                                 @RequestBody ClubReqDto clubReqDto,
                                 @RequestParam("profile") MultipartFile imageFile) {
        String kakaoId = getKakaoId(request);
        return clubService.updateClub(clubId, clubReqDto, kakaoId, imageFile);
    }

    @DeleteMapping("/{clubId}")
    @SwaggerAuth
    @ClubDeleteExplain
    public BasicResDto deleteClub(HttpServletRequest request,
                                  @PathVariable(name = "clubId") Long clubId) {
        String kakaoId = getKakaoId(request);
        return clubService.deleteClub(kakaoId, clubId);
    }

    private String getKakaoId(HttpServletRequest request) {
        return (String) request.getAttribute("kakaoId");
    }

}
