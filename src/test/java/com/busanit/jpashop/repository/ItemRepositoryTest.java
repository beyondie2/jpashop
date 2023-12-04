package com.busanit.jpashop.repository;

import com.busanit.jpashop.constant.ItemSellStatus;
import com.busanit.jpashop.entity.Item;
import com.busanit.jpashop.entity.QItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties") // 해당 테스트 실행시 환경 설정 소스 우선순위를 변경한다
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;
    // 영속성 컨텍스트 사용, 스프링 컨테이너 의존성 주입
    @PersistenceContext
    EntityManager em;

    // 가상의 아이템 10개를 만드는 셋업 메소드
    public void createItemList() {
        for (int i = 1; i <= 10 ; i++) {
            Item item = new Item();
            item.setItemNm("테스트상품" + i);
            item.setPrice(10000 + i);
            item.setStockNumber(100 + i);
            item.setItemDetail("상세정보" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            itemRepository.save(item);
        }
    }

    @Test
    void saveTest() {
        // given
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setStockNumber(100);
        item.setItemDetail("상세정보");
        item.setItemSellStatus(ItemSellStatus.SELL);
        // when
        Item saved = itemRepository.save(item);
        // then
        System.out.println(saved);
        assertEquals(item, saved);

    }

    @Test
    void findByItemTest() {
        // given
        this.createItemList();
        String findItemNm = "테스트상품3";

        // when
        List<Item> itemList = itemRepository.findByItemNm(findItemNm);

        // then
        // 눈으로 체크
        itemList.forEach(System.out::println);
        // AssertJ  : assertThat(actual).isEqualTo(expected)
        assertThat(itemList.get(0).getItemNm())
                .isEqualTo(findItemNm);
    }

    @Test
    void findByItemDetailTest() {
        // given
        this.createItemList();
        String findItemDetail = "상세정보3";

        // when
        List<Item> itemList = itemRepository.findByItemDetail(findItemDetail);

        // then
        // 눈으로 체크
        itemList.forEach(System.out::println);
        assertThat(itemList.get(0).getItemDetail())
                .isEqualTo(findItemDetail);
    }

    @Test
    void findByPriceLessThanTest() {
        // given
        this.createItemList();
        // when
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        // then
        itemList.forEach(System.out::println);
        assertThat(itemList.size()).isEqualTo(4);
    }

    @Test
    void findByItemOrItemDetail() {
        // given
        createItemList();
        // when
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품4", "상세정보4");
        // then
        itemList.forEach(System.out::println);
        assertThat(itemList.get(0).getItemNm()).isEqualTo("테스트상품4");
    }

    @Test
    void OrderBy테스트() {
        //given
        createItemList();
        // when
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        // then
        itemList.forEach(System.out::println);

    }

    @Test
    void DetailNativeTest() {
        // given
        this.createItemList();
        // when
        List<Item> itemList = itemRepository.findByItemDetailNative("상");
        // then
        itemList.forEach(System.out::println);

    }

    @Test
    void DetailJpqlTest() {
        // given
        this.createItemList();
        // when
        List<Item> itemList = itemRepository.findByItemDetailJpql("상품");
        // then
        itemList.forEach(System.out::println);
    }

    @Test
    @DisplayName("querydsl 테스트")
    void queryDslTest() {
        // given
        this.createItemList();
        // when
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        // Q타입 객체를 사용하여 Querydsl 쿼리 생성 (자동으로 생성)
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory
                .selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%상세%"))
                .orderBy(qItem.price.desc());
        List<Item> itemList = query.fetch();

        // then
        itemList.forEach(System.out::println);



    }
}