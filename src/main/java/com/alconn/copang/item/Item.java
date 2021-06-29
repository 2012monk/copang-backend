package com.alconn.copang.item;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
//@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {
    @Id @GeneratedValue
    private Long itemId;

    @Column(nullable = false)
    private String itemName;

    @CreationTimestamp
    @Column(name = "item_time",updatable = false)
    private LocalDate itemCreate;

    //상품 제거 시 상품옵션 삭제 ( 연관관계 필요 )
    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE,orphanRemoval = true)
    @Builder.Default
    private List<ItemDetail> itemDetails=new ArrayList<>();

}
