package com.busanit.jpashop.repository;

import com.busanit.jpashop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 카트 아이디와 아이템 아이디로 카트아이템을 리턴하는 쿼리 메소드
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);
}
