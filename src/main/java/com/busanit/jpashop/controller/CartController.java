package com.busanit.jpashop.controller;

import com.busanit.jpashop.dto.CartItemDto;
import com.busanit.jpashop.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // CREATE : 장바구니 담기
    @PostMapping(value = "/cart")
    @ResponseBody
    public ResponseEntity addCart(@RequestBody @Valid CartItemDto cartItemDto, Principal principal, BindingResult bindingResult) {

        // 예외처리 : 유효성 검증 에러 발생시 에러 메시지와 함께 400 리턴
        if (bindingResult.hasErrors()) {
            // String message = "";
            StringBuilder message = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                // message += fieldError.getDefaultMessage();
                message.append(fieldError.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message.toString());
        }


        String email = principal.getName();

        Long cartItemId;
        try {
            // 서비스 위임
            cartItemId = cartService.addCart(cartItemDto, email);
        } catch (Exception e) {
            // 서비스 계층 예외발생시 => 400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(cartItemId);
    }



}
