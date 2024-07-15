package youngpeople.aliali.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import youngpeople.aliali.service.BlockService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;


}
