package youngpeople.aliali.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @SwaggerAuth
    @ClubRegisterExplain
    public BasicResDto registerClub(HttpServletRequest request,
                                    @Parameter(description = "첨부파일") @RequestPart(name = "imageFile") MultipartFile imageFile,
                                    @Parameter(description = "dto") @RequestPart(name = "data") ClubReqDto clubReqDto) {
        String kakaoId = getKakaoId(request);
        return clubService.registerClub(clubReqDto, kakaoId, imageFile);
    }

    /**
     * 이미지 데이터 저장 추가 해야하져? -> 일단 했습니다.
     */
    @PutMapping(value = "/{clubId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @SwaggerAuth
    @ClubPutExplain
    public BasicResDto updateClub(HttpServletRequest request,
                                  @PathVariable(name = "clubId") Long clubId,
                                  @Parameter(description = "첨부파일") @RequestPart(name = "imageFile") MultipartFile imageFile,
                                  @Parameter(description = "dto") @RequestPart(name = "data") ClubReqDto clubReqDto) {
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
