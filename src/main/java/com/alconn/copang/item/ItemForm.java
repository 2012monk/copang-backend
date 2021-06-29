package com.alconn.copang.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemForm {
    private Long itemId;

    private String itemName;

    @Builder.Default
    private List<ItemDetailForm.DetailForm> itemDetailFormList=new ArrayList<>();

    @Override
    public String toString() {
        return "ItemForm{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemDetailFormList=" + itemDetailFormList +
                '}';
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemFormUpdate{
        @NotNull
        private  Long itemId;

        @NotNull
        private String itemName;

        @Builder.Default
        private List<ItemDetailForm.detailUpdate> itemDetailUpdateList=new ArrayList<>();
    }
}


