package com.alconn.copang.item;

import com.alconn.copang.category.Category;
import com.alconn.copang.category.CategoryRepository;
import com.alconn.copang.category.CategoryService;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.item.dto.ItemDetailForm;
import com.alconn.copang.item.dto.ItemForm;
import com.alconn.copang.item.dto.ItemViewForm;
import com.alconn.copang.item.mapper.ItemMapper;
import com.alconn.copang.search.ItemSearchCondition;
import com.alconn.copang.seller.Seller;
import com.alconn.copang.shipment.ShipmentInfo;
import com.alconn.copang.shipment.ShipmentInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemDetailService {

    private final ItemRepository itemRepository;

    private final ItemDetailRepository itemDetailRepository;

    private final ItemMapper itemMapper;

    private final ItemService itemService;

    private final CategoryRepository categoryRepository;

    private final CategoryService categoryService;

    private final ShipmentInfoRepository shipmentInfoRepository;

    //====
    private final ItemQueryRepository itemQueryRepository;

    /* =================추가 부분================= */

    public List<ItemDetailForm.MainForm> searchItems(String keyWords) {

//        List<ItemDetail> items = itemQueryRepository.searchByKeywords(keyWords, page, size);

        List<ItemDetail> items = itemQueryRepository.searchByKeywords(keyWords);
        long total = itemRepository.count();
        return itemMapper.mainPage(items);

    }

    // ================추가 끝 ======================== //

    //====
//    카테고리에 맞는 상품중 대표옵션만 출력
    public List<ItemDetailForm.MainForm> findCategpryMainList(Long categoryId)
            throws NoSuchEntityExceptions {
        List<Long> idstest = new ArrayList<>();
        List<Long> ids = categoryService.childCategoryExtract(categoryId, idstest);
        System.out.println("ids.toString() = " + ids.toString());

        List<Long> itemList = itemRepository.findCategoryItem(ids);

        ItemMainApply itemMainApply = ItemMainApply.APPLY;
        List<ItemDetail> list = itemDetailRepository
                .listItemDetailCategoryFind(itemList, itemMainApply);

        if (list.size() == 0) {
            throw new NoSuchEntityExceptions("해당하는 상품이 없습니다");
        }

        List<ItemDetailForm.MainForm> listForm = itemMapper.mainPage(list);

        return listForm;
    }


    //상품 등록
    @Transactional
    public ItemForm itemDetailListSave(ItemForm itemForm, Long sellerId) {
        //배송 저장
        //예외처리
        ShipmentInfo shipmentInfo = itemMapper.shipToEntity(itemForm);
        shipmentInfoRepository.save(shipmentInfo);

        Item item = Item.builder()
                .itemName(itemForm.getItemName())
                .itemComment(itemForm.getItemComment())
                .brand(itemForm.getBrand())
                .seller(Seller.builder().clientId(sellerId).build())
                .shipmentInfo(shipmentInfo)
                .build();
//
        Category category = categoryRepository.findById(itemForm.getCategoryId()).orElseThrow(() -> new NoSuchElementException("카테고리 정보를 확인해주세요"));
//        if(category.getChildCheck().equalsIgnoreCase("y")) {
//            throw new DataIntegrityViolationException("자식 카테고리에 등록해주세요");
//        }

        item.changeCategory(category);
        categoryRepository.save(category);

        itemService.saveItem(item);

        List<ItemDetail> itemDetailList = itemMapper.listDtoToDomainN(itemForm.getItemDetailFormList());

        //연관관계 매핑
        itemDetailList = itemDetailSaveList(item, itemDetailList);

        //재포장
        ItemForm itemDetailFormList = itemMapper.itemDetailToDto(item, itemDetailList);
        return itemDetailFormList;
    }

    //옵션 추가
    @Transactional
    public ItemViewForm itemSingle(ItemForm.ItemSingle itemSingle) {

        Item item = itemService.itemFindNum(itemSingle.getItemId());

        ItemDetail itemDetail = itemMapper.saveOneToEntity(itemSingle.getDetailForm());

        itemDetail.itemConnect(item);
        itemDetail = itemDetailRepository.save(itemDetail);
        ItemViewForm itemSingleReturn = itemMapper.detailViewForm(itemDetail);
        return itemSingleReturn;
    }


    //단일저장
    @Transactional
    public ItemDetail itemDetailSave(ItemDetail itemDetail) {
        //완성된 itemDetail이냐 아니냐를 체크해야되는건가?
        itemService.saveItem(itemDetail.getItem());
        itemDetail.itemConnect(itemDetail.getItem());
        return itemDetailRepository.save(itemDetail);
    }

    //다중저장과 연관관계 설정, enum 임시저장
    private List<ItemDetail> itemDetailSaveList(Item item, List<ItemDetail> itemDetailList) {
        for (ItemDetail itemDetail : itemDetailList) {
            itemDetail.setItemMainApply(ItemMainApply.NON);
            itemDetail.itemConnect(item);
        }
        //enum 설정하기전에 0번을 적용하는것으로 진행할게요
        itemDetailList.get(0).setItemMainApply(ItemMainApply.APPLY);
        return itemDetailRepository.saveAll(itemDetailList);
    }

    //전체출력 ( 대표 출력 )
    public ItemViewForm.MainViewForm findMainList(int page) {
        //!==
//        System.out.println("page = " + page);
//        //!==
//        if (page < 1) page = 0;
//        else page -= 1;
//
//        Pageable pageable = PageRequest.of(page, 30);
//
//
//        List<ItemDetail> list = itemDetailRepository.listItemDetailsMainFind(ItemMainApply.APPLY, pageable);
//        List<ItemDetailForm.MainForm> listForm = itemMapper.mainPage(list);
//
//        int a = itemDetailRepository.countItemDetailByItemMainApply(ItemMainApply.APPLY);
//
//        //필터링 카운트
//        ItemViewForm.MainViewForm mainViewForm = ItemViewForm.MainViewForm.builder()
//                .totalCount(a)
//                .list(listForm)
//                .build();
        ItemSearchCondition condition = new ItemSearchCondition();
        condition.setPage(page);
        return itemQueryRepository.search(condition, itemMapper);

//        for (ItemDetailForm.MainForm l : listForm) {
//            System.out.println("l.toString() = " + l.toString());
//        }

//        return listForm;
//        return mainViewForm;
    }

    //상품 상세 페이지
    //상품 아이디를 받으면 전체 상품옵션을 돌려주는 방식
    public ItemForm findItemDetailPage(Long id) {
        List<ItemDetail> list = itemDetailRepository.findItemDetailPage(id);
        ItemForm itemForm = itemMapper.itemDetailToDto(list.get(0).getItem(), list);
        return itemForm;
    }


    //삭제
    //지운 상품에 대해 정보를 돌려주는 역할을 해보도록 해보자
    @Transactional
    public ItemForm delItem(Long id) {
        ItemForm itemForm = findItemDetailPage(id);
        itemService.itemDelete(id);
        return itemForm;
    }


    //옵션 개별 제거
    @Transactional
    public ItemForm delItemDetail(Long id) {
        ItemDetail itemDetail = itemDetailRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 상품입니다"));

        List<ItemDetail> itemDetailList = new ArrayList<>();
        itemDetailList.add(itemDetail);

        ItemForm itemForm = itemMapper.itemDetailToDto(itemDetail.getItem(), itemDetailList);

        itemDetailRepository.deleteById(id);
        return itemForm;
    }

    //상품 전체 수정
    @Transactional
    public ItemForm updateItemDetail(ItemForm itemFormUpdate)
            throws NoSuchEntityExceptions {
        //풀기
        List<ItemDetailForm.DetailForm> iDetailUpdateClasses = itemFormUpdate.getItemDetailFormList();

        //item이 바뀌었는지 확인
        Item item = itemService.itemFindNum(itemFormUpdate.getItemId());

        if (itemFormUpdate.getShipmentInfoForm().getId()!=null){
        ShipmentInfo shipmentInfo= shipmentInfoRepository.findById(itemFormUpdate.getShipmentInfoForm().getId()).orElseThrow(
                ()->new NoSuchElementException("등록된 배송주소가 아닙니다"));
            shipmentInfo.update(
                    itemFormUpdate.getShipmentInfoForm().getLogisticCompany(),
                    itemFormUpdate.getShipmentInfoForm().getShippingChargeType(),
                    itemFormUpdate.getShipmentInfoForm().getFreeShipOverPrice(),
                    itemFormUpdate.getShipmentInfoForm().getReleaseDate(),
                    itemFormUpdate.getShipmentInfoForm().getShippingPrice()
            );
        }

        boolean perceive = false;
        if (!item.getItemName().equals(itemFormUpdate.getItemName()) || !item.getItemComment().equals(itemFormUpdate.getItemComment())) {
            item.updateMethod(itemFormUpdate.getItemName(), itemFormUpdate.getItemComment(), itemFormUpdate.getBrand());
            if (item.getCategory().getCategoryId() != itemFormUpdate.getCategoryId() &&
                    itemFormUpdate.getCategoryId() != null)
                categoryRepository.findById(itemFormUpdate.getCategoryId())
                        .orElseThrow(() -> new NoSuchElementException("등록된 카테고리가 아닙니다"));


            itemService.saveItem(item);
            perceive = true;
        }

        //상품 id에 해당하는 옵션조회
        List<ItemDetail> itemDetailList = itemDetailRepository.findItemDetailPage(itemFormUpdate.getItemId());

        //옵션 변경
        if (itemFormUpdate.getItemDetailFormList().size() == itemDetailList.size()) {
            for (int i = 0; i < itemDetailList.size(); i++) {
                itemDetailList.get(i).updateAllData(iDetailUpdateClasses.get(i).getPrice(),
                        iDetailUpdateClasses.get(i).getStockQuantity(),
                        iDetailUpdateClasses.get(i).getOptionName(),
                        iDetailUpdateClasses.get(i).getOptionValue(),
                        iDetailUpdateClasses.get(i).getMainImg(),
                        iDetailUpdateClasses.get(i).getSubImg());

                //연관관계
                if (perceive) itemDetailList.get(i).itemConnect(item);
            }
        }

        itemDetailRepository.saveAll(itemDetailList);

        //재포장
        ItemForm itemForm = itemMapper.updateItemForm(item, itemDetailList);
        return itemForm;
    }

    //단일 수정
    @Transactional
    public ItemViewForm itemSingleUpdate(ItemForm.ItemFormUpdateSingle updateSingle)
            throws NoSuchEntityExceptions {

        Item item = itemService.itemFindNum(updateSingle.getItemId());

        if (item.getCategory().getCategoryId() != updateSingle.getCategoryId()) {
            item.changeCategory(categoryRepository.findById(updateSingle.getCategoryId())
                    .orElseThrow(() -> new NoSuchElementException("등록된 카테고리가 아닙니다")));
        }

        item.updateMethod(updateSingle.getItemName(), updateSingle.getItemComment(), updateSingle.getBrand());

        //예외처리


        itemService.saveItem(item);


        ItemDetail itemDetail = itemDetailRepository.findById(updateSingle.getDetailUpdateClass().getItemDetailId())
                .orElseThrow(() -> new NoSuchElementException("옵션정보를 찾을 수 없습니다"));

        itemDetail.itemConnect(item);

        ItemDetailForm.DetailForm detailUpdateClass = updateSingle.getDetailUpdateClass();

        itemDetail.updateAllData(
                detailUpdateClass.getPrice(),
                detailUpdateClass.getStockQuantity(),
                detailUpdateClass.getOptionName(),
                detailUpdateClass.getOptionValue(),
                detailUpdateClass.getMainImg(),
                detailUpdateClass.getSubImg()
        );
        itemDetailRepository.save(itemDetail);

        ItemViewForm itemViewForm = itemMapper.detailViewForm(itemDetail);
        return itemViewForm;
    }
}











