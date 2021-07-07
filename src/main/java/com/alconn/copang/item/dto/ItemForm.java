package com.alconn.copang.item.dto;

import com.alconn.copang.shipment.ShipmentForm;
import com.alconn.copang.shipment.ShipmentInfo;
import com.alconn.copang.shipment.dto.ShipmentInfoForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

//저장
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemForm {

    private Long itemId;

    @NotBlank
    private String itemName;

    @NotEmpty
    private String itemComment;

    @NotNull
    private Long categoryId;

    private String brand;

    @Builder.Default
    private List<ItemDetailForm.DetailForm> itemDetailFormList=new ArrayList<>();

    @Builder.Default
    private ShipmentInfoForm shipmentInfoForm;



    //전체 수정
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemFormUpdate{

        @NotNull
        private  Long itemId;

        @NotNull
        private String itemName;

        @NotEmpty
        private String itemComment;

        private Long categoryId;

        private String brand;

        @Builder.Default
        private List<ItemDetailForm.DetailUpdateClass> itemDetailUpdateClassList =new ArrayList<>();

    }

    //옵션 수정
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemFormUpdateSingle {

        @NotNull
        private Long itemId;

        @NotBlank
        private String itemName;

        @NotEmpty
        private String itemComment;

        private String brand;

        private Long categoryId;

        private ItemDetailForm.DetailUpdateClass detailUpdateClass;
    }

    //옵션 추가
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemSingle{

       @NotNull
       private Long itemId;

       private ItemDetailForm.DetailForm detailForm;
    }

}


