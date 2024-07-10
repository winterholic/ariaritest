package youngpeople.aliali.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import youngpeople.aliali.controller.swagger.SwaggerAuth;
import youngpeople.aliali.service.MainPageService;

import static youngpeople.aliali.dto.MainPageDto.*;
import static youngpeople.aliali.controller.swagger.SwaggerExplain.*;

@Slf4j
@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainPageController {

    private final MainPageService mainPageService;

    @GetMapping("/clubs/all")
    @SwaggerAuth
    @MainAllClubsExplain
    public ClubAllDto getClubsAll(HttpServletRequest request) {
        String kakaoId = getKakaoId(request);
        return mainPageService.findClubsAll(kakaoId);
    }

    @GetMapping("/clubs/school")
    @SwaggerAuth
    @MainSchoolClubExplain
    public ClubSchoolDto getClubsSchool(HttpServletRequest request) {
        String kakaoId = getKakaoId(request);
        return mainPageService.findClubsSchool(kakaoId);
    }

    @GetMapping("/clubs/union")
    @MainUnionClubExplain
    public ClubUnionDto getClubsUnion() {
        return mainPageService.findClubsUnion();
    }

    @GetMapping("/school-category")
    @SwaggerAuth
    @MainSchoolCategoryExplain
    public ClubSchoolCategoryDto getClubsSchoolCategory(HttpServletRequest request) {
        String kakaoId = getKakaoId(request);
        return mainPageService.findClubsSchoolCategory(kakaoId);
    }

    @GetMapping("/recruitments/all/recent")
    @SwaggerAuth
    @MainAllRecruitmentRecentExplain
    public RecruitmentsDto getRecentRecruitmentsAll(HttpServletRequest request) {
        String kakaoId = getKakaoId(request);
        return mainPageService.findRecentRecruitmentsAll(kakaoId);
    }

    @GetMapping("/recruitments/all/hot")
    @SwaggerAuth
    @MainAllRecruitmentHotExplain
    public RecruitmentsDto getHotRecruitmentsAll(HttpServletRequest request) {
        String kakaoId = getKakaoId(request);
        return mainPageService.findHotRecruitmentsAll(kakaoId);
    }

    @GetMapping("/recruitments/all/deadline")
    @SwaggerAuth
    @MainAllRecruitmentDeadlineExplain
    public RecruitmentsDto getCloseDeadlineRecruitmentsAll(HttpServletRequest request) {
        String kakaoId = getKakaoId(request);
        return mainPageService.findCloseDeadlineRecruitmentsAll(kakaoId);
    }

    @GetMapping("/recruitments/school/recent")
    @SwaggerAuth
    @MainSchoolRecruitmentRecentExplain
    public RecruitmentsDto getRecentRecruitmentsSchool(HttpServletRequest request) {
        String kakaoId = getKakaoId(request);
        return mainPageService.findRecentRecruitmentsSchool(kakaoId);
    }

    @GetMapping("/recruitments/school/hot")
    @SwaggerAuth
    @MainSchoolRecruitmentHotExplain
    public RecruitmentsDto getHotRecruitmentsSchool(HttpServletRequest request) {
        String kakaoId = getKakaoId(request);
        return mainPageService.findHotRecruitmentsSchool(kakaoId);
    }

    @GetMapping("/recruitments/school/deadline")
    @SwaggerAuth
    @MainSchoolRecruitmentDeadlineExplain
    public RecruitmentsDto getCloseDeadlineRecruitmentsSchool(HttpServletRequest request) {
        String kakaoId = getKakaoId(request);
        return mainPageService.findCloseDeadlineRecruitmentsSchool(kakaoId);
    }

    @GetMapping("/recruitments/union/recent")
    @MainUnionRecruitmentRecentExplain
    public RecruitmentsDto getRecentRecruitmentsUnion() {
        return mainPageService.findRecentRecruitmentsUnion();
    }

    @GetMapping("/recruitments/union/hot")
    @MainUnionRecruitmentHotExplain
    public RecruitmentsDto getHotRecruitmentsUnion() {
        return mainPageService.findHotRecruitmentsUnion();
    }

    @GetMapping("/recruitments/union/deadline")
    @MainUnionRecruitmentDeadlineExplain
    public RecruitmentsDto getCloseDeadlineRecruitmentsUnion() {
        return mainPageService.findCloseDeadlineRecruitmentsUnion();
    }



    private String getKakaoId(HttpServletRequest request) {
        return (String) request.getAttribute("kakaoId");
    }
}
