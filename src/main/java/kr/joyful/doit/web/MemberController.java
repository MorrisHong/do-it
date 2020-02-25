package kr.joyful.doit.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MemberController {

    @PostMapping("/api/member")
    public String join(@RequestBody MemberJoinRequestDto dto) {
        log.info(dto.toString());
        return "success";
    }
}
