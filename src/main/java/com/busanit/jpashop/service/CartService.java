package com.busanit.jpashop.service;

import com.busanit.jpashop.dto.CartDetailDto;
import com.busanit.jpashop.dto.CartItemDto;
import com.busanit.jpashop.entity.Cart;
import com.busanit.jpashop.entity.CartItem;
import com.busanit.jpashop.entity.Item;
import com.busanit.jpashop.entity.Member;
import com.busanit.jpashop.repository.CartItemRepository;
import com.busanit.jpashop.repository.CartRepository;
import com.busanit.jpashop.repository.ItemRepository;
import com.busanit.jpashop.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Long addCart(CartItemDto cartItemDto, String email) {
        // 회원정보 조회
        Member member = memberRepository.findByEmail(email);
        // 상품정보 조회
        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        // 장바구니 생성 (기존 장바구니가 없는 경우만 생성)
        Cart cart = cartRepository.findByMemberId(member.getId());
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        // 장바구니 상품 생성 (기존 장바구니에 동일한 상품이 있는 경우, 수량만 추가)
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());
        if (savedCartItem != null) {
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        }

        CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
        cartItemRepository.save(cartItem);

        return cartItem.getId(); // 장바구니상품 아이디 반환
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) {
        // 장바구니
        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();
        // 장바구니가 없을 경우 빈 페이지를 반환
        if (cart == null) {
            return cartDetailDtoList;
        }

        // 장바구니 상품 조회하기 => dto 목록으로 리턴
        return cartItemRepository.findCartDetailDtoList(cart.getId());
    }
}
