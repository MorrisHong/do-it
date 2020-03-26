package kr.joyful.doit.web.member;

import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.service.member.MemberService;
import kr.joyful.doit.web.result.ApiResult;
import kr.joyful.doit.web.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/members")
    public ResponseEntity<ApiResult> join(@Valid @RequestBody MemberJoinRequestDto dto, BindingResult bindingResult) throws URISyntaxException {
        if(bindingResult.hasErrors()) {
            return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        memberService.join(dto.toEntity());
        return Result.created();
    }

    @GetMapping("/api/members/{memberId}")
    public ResponseEntity<ApiResult> findMember(@PathVariable Long memberId, @CurrentUser MemberInfo memberInfo) {
        Member member = memberService.findMemberById(memberId);
        MemberResponseDto dto = new MemberResponseDto(member);

        if (member.equals(memberInfo.getMember())) {
            dto.setOwner(true);
        }

        return Result.ok(ApiResult.blank().add("data", dto));
    }

    @PutMapping("/api/members/{memberId}")
    public ResponseEntity<ApiResult> updateMemberName(@PathVariable Long memberId,
                                              @CurrentUser MemberInfo memberInfo,
                                              @RequestBody @Valid MemberUpdateNameRequestDto dto,
                                              BindingResult bindingResult) {

        Member member = memberService.findMemberById(memberId);

        if (bindingResult.hasErrors()) {
            return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        if (!member.equals(memberInfo.getMember())) {
            throw new NotOwnerException();
        }

        Long updateMemberId = memberService.updateUsername(memberId, dto.getUsername());

        return Result.ok(ApiResult.blank().add("memberId", updateMemberId));
    }
}
