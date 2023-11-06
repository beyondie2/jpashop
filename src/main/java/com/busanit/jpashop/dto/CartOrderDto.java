package com.busanit.jpashop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CartOrderDto {
    private Long CartItemId;

    // 자기자신을 리스트로 담는 클래스 (여러 상품을 주문)
    private List<CartOrderDto> cartOrderDtoList;
}
