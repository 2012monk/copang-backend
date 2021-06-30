package com.alconn.copang.category;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id @GeneratedValue
    Long categoryId;

    String categoryName;

    Long parentId;

    //매핑
    @OneToMany(mappedBy = "category")
    @Builder.Default
    List<CategoryItem> categoryItems=new ArrayList<>();

}
