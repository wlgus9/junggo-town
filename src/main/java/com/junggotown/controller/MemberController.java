package com.junggotown.controller;

import com.junggotown.domain.Member;
import com.junggotown.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String join() {
        return "memberJoinForm";
    }

    @PostMapping("/join")
    public String join(@RequestParam("userId") String userId, @RequestParam("userPw") String userPw, Model model) {
        Member member = new Member();

        member.setUserId(userId);
        member.setUserPw(userPw);

        List<Member> members = memberService.findMemberById(userId);

        if(members.isEmpty()) {
            memberService.join(member);
            model.addAttribute("result", "회원가입 완료!");
        } else {
            model.addAttribute("result", "회원가입 실패! 존재하는 아이디입니다.");
        }

        return "member";
    }

    @GetMapping("/login")
    public String login() {
        return "memberForm";
    }

    @PostMapping("/login")
    public String login(@RequestParam("userId") String userId, @RequestParam("userPw") String userPw, Model model) throws UnsupportedEncodingException {
        Member member = new Member();
        member.setUserId(userId);
        member.setUserPw(userPw);

        boolean isSuccess = memberService.login(member);

        System.out.println("isSuccess = " + isSuccess);

        if(isSuccess) {
            return "redirect:/boards?userId=" + URLEncoder.encode(member.getUserId(), "UTF-8");
        } else {
            model.addAttribute("result", "로그인 실패!");
            return "memberForm";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "member";
    }

    @GetMapping("/update")
    public String update(@RequestParam("userId") String userId, Model model) {
        List<Member> members = memberService.findMemberById(userId);
        model.addAttribute("userId", members.get(0).getUserId());
        model.addAttribute("userPw", members.get(0).getUserPw());

        return "memberForm";
    }

    @PostMapping("/update")
    public String update(@RequestParam("userId") String userId, @RequestParam("userPw") String userPw, @RequestParam("originId") String originId, Model model) throws UnsupportedEncodingException {
        memberService.updateMemberByUserIdAndUserPw(userId, userPw, originId);

        return "redirect:/boards?userId=" + URLEncoder.encode(userId, "UTF-8");
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("userId") String userId) {
        memberService.deleteMemberByUserId(userId);

        return "member";
    }
}
