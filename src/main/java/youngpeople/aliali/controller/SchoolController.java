package youngpeople.aliali.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import youngpeople.aliali.controller.swagger.SwaggerExplain;
import youngpeople.aliali.service.SchoolService;

import java.util.List;
import static youngpeople.aliali.dto.SchoolDto.*;
import static youngpeople.aliali.controller.swagger.SwaggerExplain.*;

@Slf4j
@RestController
@RequestMapping("/school")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping("/list")
    @SchoolGetAllExplain
    public SchoolsResDto getAll() {
        return schoolService.findAll();
    }

}
