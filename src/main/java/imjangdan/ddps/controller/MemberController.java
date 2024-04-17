package imjangdan.ddps.controller;

import imjangdan.ddps.dto.response.board.ResBoardListDto;
import imjangdan.ddps.dto.response.member.ResMemberListDto;
import imjangdan.ddps.entity.Member;
import imjangdan.ddps.service.MemberService;
import imjangdan.ddps.dto.request.member.MemberLoginDto;
import imjangdan.ddps.dto.request.member.MemberRegisterDto;
import imjangdan.ddps.dto.request.member.MemberUpdateDto;
import imjangdan.ddps.dto.response.member.MemberResponseDto;
import imjangdan.ddps.dto.response.member.MemberTokenDto;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/checkId")
    public ResponseEntity<?> checkIdDuplicate(@RequestParam String email) {
        memberService.checkIdDuplicate(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/register")
    public ResponseEntity<MemberResponseDto> register(@RequestBody MemberRegisterDto memberRegisterDTO) {
        MemberResponseDto successMember = memberService.register(memberRegisterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(successMember);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberTokenDto> login(@RequestBody MemberLoginDto memberLoginDTO) {
        MemberTokenDto loginDTO = memberService.login(memberLoginDTO);
        return ResponseEntity.status(HttpStatus.OK).header(loginDTO.getToken()).body(loginDTO);
    }

    @PostMapping("/checkPwd")
    public ResponseEntity<MemberResponseDto> check(
            @AuthenticationPrincipal Member member,
            @RequestBody Map<String, String> request) {
        String password = request.get("password");
        MemberResponseDto memberInfo = memberService.check(member, password);
        return ResponseEntity.status(HttpStatus.OK).body(memberInfo);
    }

    @PutMapping("/update")
    public ResponseEntity<MemberResponseDto> update(
            @AuthenticationPrincipal Member member,
            @RequestBody MemberUpdateDto memberUpdateDTO) {
        MemberResponseDto memberUpdate = memberService.update(member, memberUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(memberUpdate);
    }


    @GetMapping("/memberlist")
    public ResponseEntity<Page<ResMemberListDto>> memberList(
        @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ResMemberListDto> listDTO = memberService.getAllMembers(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listDTO);
    }
}
