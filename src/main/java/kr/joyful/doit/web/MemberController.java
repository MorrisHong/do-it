package kr.joyful.doit.web;

import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.service.member.MemberService;
import kr.joyful.doit.web.dto.MemberJoinRequestDto;
import kr.joyful.doit.web.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

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

    @GetMapping("/api/member/{memberId}")
    public ResponseEntity<MemberResponseDto> findMember(@PathVariable Long memberId) {
        Member member = memberService.findMember(memberId);
        MemberResponseDto dto = new MemberResponseDto(member);
        return ResponseEntity.ok(dto);
    }
}
