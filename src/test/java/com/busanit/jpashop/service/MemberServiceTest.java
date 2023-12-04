package com.busanit.jpashop.service;

import com.busanit.jpashop.dto.MemberDto;
import com.busanit.jpashop.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional      // 해당 클래스 내의 메소드는 테스트 실행 후 롤백 처리
class MemberServiceTest {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("회원 가입 테스트")
    void saveMember() {
        // given
        Member member = createMember();

        // when : 서비스계층에서 saveMember 메서드 호출했을 때
        Member savedMember = memberService.saveMember(member);

        // then : given에서 주어진 member와 같은 내용인지 검증
        assertThat(savedMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(savedMember.getRole()).isEqualTo(member.getRole());
    }

    private Member createMember() {
        MemberDto memberDto = MemberDto.builder()
                .email("test@test.com")
                .name("홍길동")
                .address("부산시")
                .password("1234")
                .build();
        Member member = Member.createMember(memberDto, passwordEncoder);
        return member;
        //return null;
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    void test2() {
        // given
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);

        // when
        // assertThrows(예외타입, 실행문) : 실행시 해당 예외타입이 발생하면 검증 통과
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);
        });

        // then
        assertThat(e.getMessage()).isEqualTo("가입된 회원입니다.");
    }
}