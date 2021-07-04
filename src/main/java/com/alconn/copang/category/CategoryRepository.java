package com.alconn.copang.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.layer<4")
    List<Category> findLayer();

    List<Category> findByParentId(Long aLong);

}
