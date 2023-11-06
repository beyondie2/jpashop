package com.busanit.jpashop.service;

import com.busanit.jpashop.dto.OrderDto;
import com.busanit.jpashop.dto.OrderHistDto;
import com.busanit.jpashop.dto.OrderItemDto;
import com.busanit.jpashop.entity.*;
import com.busanit.jpashop.repository.ItemImgRepository;
import com.busanit.jpashop.repository.ItemRepository;
import com.busanit.jpashop.repository.MemberRepository;
import com.busanit.jpashop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email) {
        // 상품 정보 가져오기
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        // 회원 정보 가져오기
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();
        // 상품과 개수로 OrderItem 생성 => 재고 줄이기
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);
        
        // 주문 생성
        Order order = Order.createOrder(member, orderItemList);

        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)     // 읽기 전용 트랜잭션 성능 최적화
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {

        // 1. 유저 id(email)을 이용하여, 주문 목록을 조회
        List<Order> orders = orderRepository.findOrder(email, pageable);
        // 2. 총 개수 조회
        Long totalCount = orderRepository.countOrder(email);
        // 3. 주문 리스트 순회하면서 뷰페이지 전달할 DTO 생성 : 주문Dto 목록
        List<OrderHistDto> orderHistDtoList = new ArrayList<>();
        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            // 순회하면서 주문아이템DTO
            for (OrderItem orderItem : orderItems) {
                // 4. 상품 대표 이미지 조회
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtoList.add(orderHistDto);
        }

        // 5. 페이지 객체 리턴
        return new PageImpl<>(orderHistDtoList, pageable, totalCount);
    }

    public void cancelOrder(Long orderId) {
        // 주문 정보를 영속성 컨텍스트에 가져오기 : 변경감지
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();

    }

    // 장바구니 상품 목록(여러개 주문)
    public Long orders(List<OrderDto> orderDtoList, String email) {

        // 주문 상품 리스트 만들기
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderDto orderDto : orderDtoList) {
            // 상품 조회
            Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
            // 주문 상품 만들기
            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            orderItemList.add(orderItem);
        }

        // 회원정보조회
        Member member = memberRepository.findByEmail(email);

        // 주문 엔티티 생성
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }
}
