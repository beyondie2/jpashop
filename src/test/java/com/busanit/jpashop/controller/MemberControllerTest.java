package com.busanit.jpashop.controller;

import com.busanit.jpashop.dto.MemberDto;
import com.busanit.jpashop.entity.Member;
import com.busanit.jpashop.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc       // 모의 MVC 설정을 통해 테스트
class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired  // 모의 객체
    private MockMvc mockMvc;

    @Autowired  // 암호화
    private PasswordEncoder passwordEncoder;

    public Member createMember(String email, String password) {
        MemberDto memberDto = new MemberDto();
        memberDto.setEmail(email);
        memberDto.setPassword(password);
        memberDto.setName("홍길동");
        memberDto.setAddress("부산");
        // dto -> entity
        Member member = Member.createMember(memberDto, passwordEncoder);
        return memberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginTest() throws Exception {
        // 회원 가입
        Member member = createMember("test@test.com", "1234");
        // 모의 객체로 로그인 확인
        mockMvc.perform(SecurityMockMvcRequestBuilders.formLogin()
                .loginProcessingUrl("/members/login")       // 로그인
                .userParameter("email")
                .user("test@test.com").password("1234"))
                .andDo(print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated());    // 테스트 검증 성공할 경우 통과
    }

    @Test
    @DisplayName("로그인 실패 태스트")
    void loginTestFail() throws Exception {
        // 회원 가입
        Member member = createMember("test@test.com", "1234");
        // 모의 객체로 로그인 확인
        mockMvc.perform(SecurityMockMvcRequestBuilders.formLogin()
                        .loginProcessingUrl("/members/login")       // 로그인
                        .userParameter("email")
                        .user("test@test.com").password("12345"))
                .andDo(print())
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());    // 테스트 검증
    }
}