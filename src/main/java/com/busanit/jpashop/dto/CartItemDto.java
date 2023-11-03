package com.busanit.jpashop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

// 상품 상세페이지 : 장바구니 담기 클릭할 때 전달되는 데이터
@Getter @Setter
public class CartItemDto {

    @NotNull(message = "상품 아이디는 필수 입력 값입니다.")
    private Long itemId;     // 상품 아이템

    @Min(value = 1, message = "최소수량은 1입니다.")
    private int count;      // 수량
}
