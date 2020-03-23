package kr.joyful.doit.web.member;

import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.service.member.MemberService;
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
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        memberService.join(dto.toEntity());
        URI url = new URI("/login");
        return ResponseEntity.created(url).build();
    }

    @GetMapping("/api/member/{memberId}")
    public ResponseEntity<MemberResponseDto> findMember(@PathVariable Long memberId, @CurrentUser MemberInfo memberInfo) {
        Member member = memberService.findMemberById(memberId);
        MemberResponseDto dto = new MemberResponseDto(member);

        if (member.equals(memberInfo.getMember())) {
            dto.setOwner(true);
        }

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/api/member/{memberId}")
    public ResponseEntity<?> updateMemberName(@PathVariable Long memberId,
                                              @CurrentUser MemberInfo memberInfo,
                                              @RequestBody @Valid MemberUpdateNameRequestDto dto,
                                              BindingResult bindingResult) {

        Member member = memberService.findMemberById(memberId);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        if (!member.equals(memberInfo.getMember())) {
            throw new NotOwnerException();
        }

        Long updateMemberId = memberService.updateUsername(memberId, dto.getUsername());

        return ResponseEntity.ok(updateMemberId);
    }
}
