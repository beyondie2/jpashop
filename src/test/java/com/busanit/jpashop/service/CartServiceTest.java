package com.busanit.jpashop.service;

import com.busanit.jpashop.constant.ItemSellStatus;
import com.busanit.jpashop.dto.CartItemDto;
import com.busanit.jpashop.dto.OrderDto;
import com.busanit.jpashop.entity.CartItem;
import com.busanit.jpashop.entity.Item;
import com.busanit.jpashop.entity.Member;
import com.busanit.jpashop.repository.CartItemRepository;
import com.busanit.jpashop.repository.ItemRepository;
import com.busanit.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired
    CartService cartService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Test
    void addCart() {
        // given : cartItemDto
        Item item = getItem();
        Member member = getMember();
        String email = member.getEmail();

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setItemId(item.getId());
        cartItemDto.setCount(10);

        // when
        Long cartItemId = cartService.addCart(cartItemDto, email);

        // then
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);

        System.out.println("장바구니상품 수량 : " + cartItem.getCount());
        System.out.println("장바구니상품DTO 수량 : " + cartItemDto.getCount());

        Assertions.assertThat(cartItem.getCount()).isEqualTo(cartItemDto.getCount());

    }

    // 모의 멤버
    private Member getMember() {
        Member member = new Member();
        member.setEmail("test@test.com");
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    // 모의 아이템
    private Item getItem() {
        Item item = new Item();
        item.setItemNm("상품명");
        item.setPrice(10000);
        item.setItemDetail("상품상세");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        Item savedItem = itemRepository.save(item);
        return savedItem;
    }
}