package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class RootController {
    public final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/")
    public String root() {
        log.info("root");
        return "redirect:swagger-ui.html";
    }
}
