package com.busanit.jpashop.repository;

import com.busanit.jpashop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // 멤버 아이디로 장바구니를 찾는 쿼리 메소드
    Cart findByMemberId(Long memberId);

}
