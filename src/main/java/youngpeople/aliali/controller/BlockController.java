package youngpeople.aliali.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import youngpeople.aliali.dto.BasicResDto;
import youngpeople.aliali.service.BlockService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    @PostMapping
    public BasicResDto createBlock(HttpServletRequest request, @RequestBody BlockReqDto blockReqDto) {
        String kakaoId = getKakaoId(request);
        return blockService.createBlock(kakaoId, blockReqDto);
    }

    @Data
    @AllArgsConstructor
    public static class BlockReqDto {
        private Long targetId;
    }

    private String getKakaoId(HttpServletRequest request) {
        return (String) request.getAttribute("kakaoId");
    }

}
