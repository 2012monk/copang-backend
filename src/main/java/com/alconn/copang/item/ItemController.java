package com.alconn.copang.item;

import com.alconn.copang.annotations.InjectId;
import com.alconn.copang.aop.GlobalExceptionHandler;
import com.alconn.copang.client.Role;
import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.item.dto.ItemDetailForm;
import com.alconn.copang.item.dto.ItemForm;
import com.alconn.copang.item.dto.ItemViewForm;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/item")
@Slf4j
public class ItemController {

    private final ItemDetailService itemDetailService;

    private final GlobalExceptionHandler globalExceptionHandler;

    //카테고리 클릭 시 자식 카테고리 조회하여 상품출력
    @GetMapping("/list/categoryid={categoryId}")
        public ResponseMessage<List<ItemDetailForm.MainForm>> categoryItemlist(@PathVariable(name = "categoryId")Long id){
            List<ItemDetailForm.MainForm> itemDetailFormList=itemDetailService.findCategpryMainList(id);
            return ResponseMessage.<List<ItemDetailForm.MainForm>>builder()
                    .message("카테고리상품리스트")
                    .data(itemDetailFormList)
                    .code(200)
                    .build();
        }



    //저장
    @PostMapping("/add")
    public ResponseMessage<ItemForm> add(@Valid @RequestBody ItemForm itemForm,
        @InjectId(role = Role.SELLER) Long sellerId) {
        ItemForm itemFormReturn = itemDetailService.itemDetailListSave(itemForm, sellerId);
        return ResponseMessage.<ItemForm>builder()
            .message("저장완료")
            .data(itemFormReturn)
            .code(200)
            .build();
    }

    //단일 옵션추가
    @PostMapping("/add/detail")
    public ResponseMessage<ItemViewForm> addOne(@Valid @RequestBody ItemForm.ItemSingle itemForm)
        throws NoSuchEntityExceptions {
        ItemViewForm itemFormReturn = itemDetailService.itemSingle(itemForm);
        return ResponseMessage.<ItemViewForm>builder()
            .message("옵션추가완료")
            .data(itemFormReturn)
            .code(200)
            .build();
    }

    //단일 수정
    @PutMapping("/update")
    public ResponseMessage<ItemViewForm> itemUpdate(
        @Valid @RequestBody ItemForm.ItemFormUpdateSingle itemForm) throws NoSuchEntityExceptions {
        ItemViewForm itemForm2 = itemDetailService.itemSingleUpdate(itemForm);
        return ResponseMessage.<ItemViewForm>builder()
            .message("상품단일수정목록")
            .data(itemForm2)
            .code(200)
            .build();
    }


    //메인 대표이미지만 출력
    @GetMapping("/list")
    public ResponseMessage<List<ItemDetailForm.MainForm>> list() {
        List<ItemDetailForm.MainForm> itemDetailFormList = itemDetailService.findMainList();
        return ResponseMessage.<List<ItemDetailForm.MainForm>>builder()
            .message("대표리스트")
            .data(itemDetailFormList)
            .code(200)
            .build();
    }

    //상세페이지
    @GetMapping("/list/itemid={itemId}")
    public ResponseMessage<ItemForm> itemDetailPageResponse(
        @PathVariable(name = "itemId") Long id) {
        ItemForm itemForm = itemDetailService.findItemDetailPage(id);
        return ResponseMessage.<ItemForm>builder()
            .message("상품상세페이지")
            .data(itemForm)
            .code(200)
            .build();

    }

    //1. 상품 삭제
    @DeleteMapping("/delete/{itemId}")
    public ResponseMessage<ItemForm> itemDel(@PathVariable(name = "itemId") Long id) {
        ItemForm itemForm = itemDetailService.delItem(id);
        return ResponseMessage.<ItemForm>builder()
            .message("상품삭제된목록")
            .data(itemForm)
            .code(200)
            .build();
    }

    //2. 상품옵션 하나 삭제
    @DeleteMapping("/delete/item-detail/{itemDetailId}")
    public ResponseMessage<ItemForm> itemDetailDel(@PathVariable(name = "itemDetailId") Long id) {
        ItemForm itemForm
            = itemDetailService.delItemDetail(id);
        return ResponseMessage.<ItemForm>builder()
            .message("상품삭제된목록")
            .data(itemForm)
            .code(200)
            .build();
    }

    //상품 전체 수정 + 카테고리 수정 포함
    @PutMapping("/update/list")
    public ResponseMessage<ItemForm.ItemFormUpdate> itemUpdate(
        @Valid @RequestBody ItemForm.ItemFormUpdate itemForm) throws NoSuchEntityExceptions {
        ItemForm.ItemFormUpdate itemForm2 = itemDetailService.updateItemDetail(itemForm);

        return ResponseMessage.<ItemForm.ItemFormUpdate>builder()
            .message("상품전체수정목록")
            .data(itemForm2)
            .code(200)
            .build();
    }

}
