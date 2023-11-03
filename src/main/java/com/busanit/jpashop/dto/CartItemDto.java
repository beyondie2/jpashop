package com.busanit.jpashop.dto;

import lombok.Getter;
import lombok.Setter;

// 상품 상세페이지 : 장바구니 담기 클릭할 때 전달되는 데이터
@Getter @Setter
public class CartItemDto {

    private Long itemId;     // 상품 아이템
    
    private int count;      // 수량
}
