package kr.joyful.doit.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(value = {"/", "/login", "/register"})
    public String entry() {
        return "index";
    }
}
