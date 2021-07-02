package com.alconn.copang.cart;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class CartItemForm {

    private Long cartId;

    private Long itemDetailId;

    private Long itemId;

    @NotBlank
    private String itemName;

    @Positive
    private Integer price;

    @Positive
    private Integer amount;

    @NotBlank
    private String optionName;

    @NotBlank
    private String optionValue;

    @NotBlank
    private String mainImg;

    private String subImg;

    private Integer unitTotal;

//    @JsonFormat(pattern = "yyyy//MM/dd", timezone = "Asia/Seoul", shape = Shape.STRING)
    @JsonFormat(timezone = "Seoul/Asia", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registerDate;


    public void calculateUnitTotal() {
        this.unitTotal = this.price * this.amount;
    }
}
