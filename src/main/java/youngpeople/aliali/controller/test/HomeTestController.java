package youngpeople.aliali.controller.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/")
public class HomeTestController {

    @GetMapping
    public LocalDateTime home() {
        return LocalDateTime.now();
    }
}
