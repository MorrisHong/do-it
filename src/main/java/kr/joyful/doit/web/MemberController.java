package kr.joyful.doit.web;

import kr.joyful.doit.service.MemberService;
import kr.joyful.doit.web.dto.MemberJoinRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/member")
    public ResponseEntity<?> join(@Valid @RequestBody MemberJoinRequestDto dto, BindingResult bindingResult) throws URISyntaxException {
        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().stream().map(ObjectError::toString).forEach(log::error);
            return ResponseEntity.badRequest().build();
        }
        memberService.join(dto.toEntity());
        URI url = new URI("/login");
        return ResponseEntity.created(url).build();
    }
}
