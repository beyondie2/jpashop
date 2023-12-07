package com.busanit.jpashop.controller;

import com.busanit.jpashop.dto.ItemDto;
import com.busanit.jpashop.dto.ItemFormDto;
import com.busanit.jpashop.dto.ItemSearchDto;
import com.busanit.jpashop.entity.Item;
import com.busanit.jpashop.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping("/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto,
                          BindingResult bindingResult,                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model ) {
        // 유효성 검증 : 통과하지 못한 경우 폼으로
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        // 상품 이미지 파일 1개를 필수적으로 넣는 유효성 검증 추가
        if (itemImgFileList.get(0).isEmpty()) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        // 아이템 DB에 저장
        itemService.saveItem(itemFormDto, itemImgFileList);
        // 리다이렉트
        return "redirect:/";
    }

    // 아이템 수정 Get 요청
    @GetMapping("/admin/item/{itemId}")
    public String adminItemDtl(@PathVariable("itemId") Long itemId, Model model) {
        
        try {
            // 아이템 서비스에 위임하여 dto 리턴
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            // 엔티티가 존재할 경우, dto를 모델에 담아 뷰로 전달
            model.addAttribute("itemFormDto", itemFormDto);
            // 경로변수(itemId)에 해당하는 엔티티가 없을 경우
        } catch(EntityNotFoundException e) {
            // 상품등록 페이지로 리다이렉트
            return "redirect:/admin/item/new";
        }

        return "item/itemForm";
    }

    @PostMapping("/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model ) {

        // 유효성 검증 : 통과하지 못한 경우 폼으로
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }
        // 서비스 계층에 수정 비즈니스 로직 위임
        itemService.updateItem(itemFormDto, itemImgFileList);
        
        return "redirect:/";
    }

    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(Model model, ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page) {
        // page가 null일 경우 : 시작(0)부터, 존재할 경우 page번호로 보여준다.
        Pageable pageable = PageRequest.of( page.isPresent() ? page.get() : 0, 3);

        // 서비스 계층에서 item 페이지 가져오기
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);


        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);

        // 시작페이지, 마지막 페이지, 최대 페이지 구하기
        int maxPage = 5;
        int start = (items.getNumber() / maxPage) * maxPage + 1;
        int end = 0;

        if (items.getTotalPages() == 0) {
            end = 1;
        } else if (start+(maxPage-1) < items.getTotalPages()) {
            end = start + maxPage - 1;
        } else {
            end = items.getTotalPages();
        }

        model.addAttribute("maxPage", maxPage);
        model.addAttribute("start", start);
        model.addAttribute("end", end);


        return "item/itemMng";
    }

    // 상세 페이지
    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(@PathVariable Long itemId, Model model) {
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl";
    }


}
