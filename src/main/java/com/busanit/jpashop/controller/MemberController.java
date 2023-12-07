package com.busanit.jpashop.controller;

import com.busanit.jpashop.dto.MemberDto;
import com.busanit.jpashop.entity.Member;
import com.busanit.jpashop.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/members/new")
    public String memberForm(Model model) {
        model.addAttribute("memberDto",new MemberDto());
        return "member/memberForm";
    }
    // 검증이 필요한 객체 앞에 valid 선언,
    // BindingResult 객체를 파라미터로 추가해서 결과를 담아준다.
    @PostMapping("/members/new")
    public String memberForm(@Valid MemberDto memberDto,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }  // 유효성 검증에 에러가 있으면, 다시 회원가입 페이지
        try {
            // Post - Redirect - Get Pattern
            // 생성메서드 dto -> entity
            Member member = Member.createMember(memberDto, passwordEncoder);
            // entity -> db에 저장
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        // 리다이렉트
        return "redirect:/";
}

    @GetMapping("/members/login")
    public String loginMember() {
        return "member/memberLoginForm";
    }

    @GetMapping("/members/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디와 비밀번호를 확인해주세요.");
        return "member/memberLoginForm";
    }



}
