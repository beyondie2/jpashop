package com.busanit.jpashop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDetailDto {
    private Long cartItemId;        // 장바구니 상품ID
    private String ItemNm;          // 상품명
    private Integer price;          // 금액
    private Integer count;          // 수량
    private String ImgUrl;          // 상품 이미지 경로

    public CartDetailDto(Long cartItemId, String itemNm, Integer price, Integer count, String imgUrl) {
        this.cartItemId = cartItemId;
        ItemNm = itemNm;
        this.price = price;
        this.count = count;
        ImgUrl = imgUrl;
    }
}
