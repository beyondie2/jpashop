package com.busanit.jpashop.controller;

import com.busanit.jpashop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // CREATE : 장바구니 담기
    @PostMapping(value = "/cart")
    @ResponseBody
    public ResponseEntity addCart() {

        // 예외처리

        cartService.addCart();

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


}
