package youngpeople.aliali.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import youngpeople.aliali.controller.swagger.SwaggerAuth;
import youngpeople.aliali.controller.swagger.SwaggerExplain;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.dto.PostDto;
import youngpeople.aliali.service.PostService;

import java.util.List;

import static youngpeople.aliali.controller.swagger.SwaggerExplain.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/club/{clubId}/post")
public class PostController {
    private final PostService postService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @SwaggerAuth
    @WritePostExplain
    public BasicResDto addPost(HttpServletRequest request,
                               @PathVariable("clubId") Long clubId,
                               @Parameter(description = "imageFiles") @RequestPart(name = "imageFiles") List<MultipartFile> imageFiles,
                               @Parameter(description = "postReqDto") @RequestPart(name = "postReqDto") PostDto.PostReqDto postReqDto){
        log.info("♠♠♠♠♠♠♠♠♠♠♠♠♠♠♠♠♠♠♠♠♠♠♠♠♠♠♠♠♠♠♠{}, {}, {}", clubId, postReqDto, imageFiles);
        String kakaoId = getKakaoId(request);
        return postService.SavePost(postReqDto, clubId, kakaoId, imageFiles);
    }

//    @GetMapping("/list/{pageId}")
//    public BasicResDto findList(@PathVariable("clubId") Long clubId, @PathVariable("pageId") int pageId){
//
//    }

    private String getKakaoId(HttpServletRequest request) {
        return (String) request.getAttribute("kakaoId");
    }
}
