package com.busanit.jpashop.repository;

import com.busanit.jpashop.dto.CartDetailDto;
import com.busanit.jpashop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 카트 아이디와 아이템 아이디로 카트아이템을 리턴하는 쿼리 메소드
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    // DTO 목록으로 바로 프로젝션하는 JPQL문 작성
    // new 키워드와 해당 DTO의 참조명(패키지.클래스명) 그리고, 생성자 파라미터 순서로 명시
    // DTO에 해당하는 데이터 필드(5개)를 3가지 엔티티(테이블) 조인하여 해당 멤버(장바구니)에 담긴 필요한 데이터를 조회
    // 장바구니Id, 상품명, 가격, 수량, 대표이미지URL
    @Query("select new com.busanit.jpashop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repImgYn = 'Y' " +
            "order by ci.regTime desc")
    List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);

    /*
SELECT
    ci.cart_item_id,
    i.item_nm,
    i.price,
    ci.count,
    im.img_url
FROM
    cart_item ci
INNER JOIN
    item_img im ON ci.item_id = im.item_id
INNER JOIN
    item i ON ci.item_id = i.item_id
WHERE
    ci.cart_id = 1
    AND im.rep_img_yn = 'Y'
ORDER BY
    ci.reg_time DESC
    * */
}
