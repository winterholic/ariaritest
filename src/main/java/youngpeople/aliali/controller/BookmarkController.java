package youngpeople.aliali.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import youngpeople.aliali.controller.swagger.SwaggerAuth;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.service.BookmarkService;

import static youngpeople.aliali.controller.swagger.SwaggerExplain.*;

@Slf4j
@RestController
@RequestMapping("/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/register")
    @SwaggerAuth
    @BookmarkRegisterExplain
    public BasicResDto registerBookmarks(HttpServletRequest request,
                                         @RequestBody BookmarkRegisterReqDto bookmarkRegisterReqDto) {
        String kakaoId = getKakaoId(request);
        return bookmarkService.registerBookmark(kakaoId, bookmarkRegisterReqDto.getClubId());
    }

    @PostMapping("/delete")
    @SwaggerAuth
    @BookmarkDeleteExplain
    public BasicResDto cancelBookmark(HttpServletRequest request,
                                      @RequestBody BookmarkDeleteReqDto bookmarkDeleteReqDto) {
        String kakaoId = getKakaoId(request);
        return bookmarkService.cancelBookmark(kakaoId, bookmarkDeleteReqDto.getBookmarkId());
    }


    private String getKakaoId(HttpServletRequest request) {
        return (String) request.getAttribute("kakaoId");
    }

    @Data
    @NoArgsConstructor
    public static class BookmarkRegisterReqDto {
        private Long clubId;
    }

    @Data
    @NoArgsConstructor
    public static class BookmarkDeleteReqDto {
        Long bookmarkId;
    }

}
