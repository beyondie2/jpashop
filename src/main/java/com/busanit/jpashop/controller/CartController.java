package com.busanit.jpashop.controller;

import com.busanit.jpashop.dto.CartItemDto;
import com.busanit.jpashop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // CREATE : 장바구니 담기
    @PostMapping(value = "/cart")
    @ResponseBody
    public ResponseEntity addCart(@RequestBody CartItemDto cartItemDto, Principal principal) {
        // 예외처리

        // 서비스 위임
        String email = principal.getName();
        Long cartItemId = cartService.addCart(cartItemDto, email);

        return ResponseEntity.status(HttpStatus.OK).body(cartItemId);
    }


}
