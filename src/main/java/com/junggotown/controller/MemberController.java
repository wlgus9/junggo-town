package com.junggotown.controller;

import com.junggotown.domain.Member;
import com.junggotown.form.MemberForm;
import com.junggotown.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping(value={"/", ""})
    public String home() {
        return "member";
    }

    @GetMapping("/join")
    public String join() {
        return "memberJoinForm";
    }

    @PostMapping("/join")
    public String join(MemberForm memberForm) {
        Member member = new Member();
        member.setUserId(memberForm.getUserId());
        member.setUserPw(memberForm.getUserPw());

        memberService.join(member);

        return "redirect:/members";
    }
}
