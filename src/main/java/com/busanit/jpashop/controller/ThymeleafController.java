package com.busanit.jpashop.controller;


import com.busanit.jpashop.dto.ItemDto;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafController {

    @GetMapping("/ex01")
    public String ex01(Model model) {
        model.addAttribute("data", "안녕하세요~ 타임리프입니다.");
        return "thymeleaf/ex01";
    }

    @GetMapping("/ex02")
    public String ex02(Model model) {
        // 빌더 패턴을 사용해서 itemDto 생성
        ItemDto itemDto = ItemDto.builder()
                .itemNm("상품명1")
                .itemDetail("상품상세")
                .price(10000)
                .regTime(LocalDateTime.now())
                .build();
        // 모델로 전달
        model.addAttribute("itemDto", itemDto);
        // 타임리프 뷰 페이지로 데이터가 전달
        return "thymeleaf/ex02";
    }

    @GetMapping("/ex03")
    public String ex03(Model model) {
        // 10개의 itemDto가 담긴 목록 만들기
        ArrayList<ItemDto> itemDtoList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ItemDto itemDto = ItemDto.builder()
                    .itemNm("상품명" + i)
                    .itemDetail("상품상세" + i)
                    .price(10000+ i)
                    .regTime(LocalDateTime.now())
                    .build();
            itemDtoList.add(itemDto);
        }
        // 모델에 저장
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleaf/ex03";
    }

    @GetMapping("/ex04")
    public String ex04(Model model) {
        // 10개의 itemDto가 담긴 목록 만들기
        ArrayList<ItemDto> itemDtoList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            ItemDto itemDto = ItemDto.builder()
                    .itemNm("상품명" + i)
                    .itemDetail("상품상세" + i)
                    .price(10000+ i)
                    .regTime(LocalDateTime.now())
                    .build();
            itemDtoList.add(itemDto);
        }
        // 모델에 저장
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleaf/ex04";
    }

    @GetMapping("/ex05")
    public String ex05(Model model) {
        // 타임리프 링크 예제
        return "thymeleaf/ex05";
    }

    @GetMapping("/ex06")
    public String ex06(Model model, String param1, String param2) {
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        // 쿼리 파라미터 예제
        return "thymeleaf/ex06";
    }

    @GetMapping("/ex07")
    public String ex07(Model model) {
        // 타임리프 레이아웃 예제
        return "thymeleaf/ex07";
    }
}
