package com.alconn.copang.category;

import com.alconn.copang.category.dto.CategoryRequest;
import com.alconn.copang.category.dto.CategoryView;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import static java.util.stream.Collectors.groupingBy;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    final CategoryRepository categoryRepository;

    final CategoryMapper categoryMapper;

    //자식 등록시 상위 객체가 있으면 Y로 변경
    //null은 컨트롤러단에서 들어오지못함
    //카테고리 등록시
    @Transactional
    public CategoryView save(CategoryRequest.CategorySave categorySave) throws NoSuchEntityExceptions {
        Category category=categoryMapper.topToEntity(categorySave);

        //최상위 카테고리 셋팅
        if(categorySave.getParentId()==0){
            category.changeCategoryprentId(0L);
            category.changeChildCheck("N");
            category.changeLayer(1);
        }

        else {
            //부모 컬럼 수정 ( 부모가 없다면 parentid는 0으로 들어와야한다 )
            Category category2=categoryRepository.findById(category.getParentId()).orElseThrow((()->new NoSuchEntityExceptions("등록된 parentId가 없습니다")));
            category2.changeChildCheck("Y");
            categoryRepository.save(category2);
            category.changeChildCheck("N");
            category.changeLayer(category2.getLayer()+1);
        }
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }


    @Transactional
    public CategoryView categorydelete(Long id) throws NoSuchElementException{

        //id값이 없을때 에러발생
        Category categor=categoryRepository.findById(id).orElseThrow(()->new NoSuchElementException("등록된 카테고리가 아닙니다"));

        //자식 데이터가 있다면 삭제불가능 -> 컨트롤러에서 나누기
        if(categor.getChildCheck().equals("Y")){
            return null;
        }

        CategoryView categoryView = categoryMapper.toDto(categor);
        categoryRepository.deleteById(id);
        return categoryView;
    }


//  수정
    @Transactional
    public CategoryView categoryupdate (CategoryRequest.CategoryUpdate categoryUpdate) throws NoSuchElementException{
        Category categor=categoryRepository.findById(categoryUpdate.getCategoryId()).orElseThrow(()->new NoSuchElementException("등록된 카테고리가 아닙니다"));
        //다른 카테고리로 이전하는 시나리오
        //자신을 부모로 참조하는 다른 테이블은 없어야 한다
        //추가 후 부모테이블 자식이 없다면 변경한다
        //바꾸려는 테이블의 자식은 없어야한다
        if(categoryUpdate.getParentId()!=null && categor.getChildCheck().equals("N")){
            try{
                Category parent=categoryRepository.findById(categoryUpdate.getParentId()).orElseThrow(()->new NoSuchElementException("등록된 부코 카테고리가 아닙니다"));
                //참조하는 자식이 없는 상태면 Y로 변경
                if(parent.getChildCheck().equals("N")){
                    parent.changeChildCheck("Y");
                    categoryRepository.save(parent);
                }
            }catch (NoSuchElementException e){
                throw new NoSuchElementException("부모카테고리가 등록되어있지 않습니다");
            }
        }

        categor.changeCategoryName(categoryUpdate.getCategoryName());
        categoryRepository.save(categor);
        return categoryMapper.toDto(categor);
    }

//======전체 조회용====
        public CategoryView.CategoryListDto rootCategory(Long id){
            Map<Long, List<CategoryView.CategoryListDto>> parentGroup = categoryRepository.findAll()
                    .stream()
                    .   map(category -> CategoryView.CategoryListDto.builder()
                            .categoryId(category.getCategoryId())
                            .categoryName(category.getCategoryName())
                            .parentId(category.getParentId())
                            .build()).collect(groupingBy(c->c.getParentId()));

            CategoryView.CategoryListDto rootCategoryDto=  CategoryView.CategoryListDto.builder()
                    .categoryId(id)
                    .categoryName("Root")
                    .cildCategory(null)
                    .build();
            childCategoryadd(rootCategoryDto, parentGroup);


            return rootCategoryDto;
        }
    private void childCategoryadd(CategoryView.CategoryListDto rootCategoryDto, Map<Long, List<CategoryView.CategoryListDto>> parentGroup) {
        //rootCategoryDto로 자식 카테고리를 찾는다
        List<CategoryView.CategoryListDto> childCategory = parentGroup.get(rootCategoryDto.getCategoryId());
        //종료 조건
        if (childCategory == null)
            return;

        //subcategory 셋팅
        rootCategoryDto.changeSubCategory(childCategory);

        //재귀적으로 childCategory들에 대해서도 수행
        childCategory.stream().forEach(s -> {
            childCategoryadd(s, parentGroup);
        });
    }
//====================
// =====카테고리 3분류가지만 출력====
        public CategoryView.CategoryListDto layerCategory(){
            Map<Long, List<CategoryView.CategoryListDto>> parentGroup = categoryRepository.findLayer()
                    .stream()
                    .   map(category -> CategoryView.CategoryListDto.builder()
                            .categoryId(category.getCategoryId())
                            .categoryName(category.getCategoryName())
                            .parentId(category.getParentId())
                            .build()).collect(groupingBy(c->c.getParentId()));

            CategoryView.CategoryListDto rootCategoryDto=  CategoryView.CategoryListDto.builder()
                    .categoryId(0l)
                    .categoryName("Root")
                    .cildCategory(null)
                    .build();
            childCategoryadd(rootCategoryDto, parentGroup);


            return rootCategoryDto;
        }
//====================


}
