package com.alconn.copang.review;

import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.UnauthorizedException;
import com.alconn.copang.review.ReviewForm.Request;
import com.alconn.copang.review.ReviewForm.Response;
import com.alconn.copang.review.ReviewForm.Update;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository repository;

    private final ReviewMapper mapper;


    public List<Response> getReviewByItem(Long itemId) {

        List<Review> list = repository.findReviewsByOrderItem_ItemDetail_Item_ItemId(itemId,
            Sort.by(Direction.ASC, "rating"));

        return list.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public Response postReview(Request requestForm, Long clientId) {
        Review review = mapper.toEntity(requestForm, clientId);

        repository.save(review);
        return mapper.toDto(review);
    }

    @Transactional
    public Response updateReview(Update request, Long clientId, Long reviewId)
        throws NoSuchEntityExceptions, UnauthorizedException {

        Review review = repository.findById(reviewId).orElseThrow(NoSuchEntityExceptions::new);

        if (!review.getWriter().getClientId().equals(clientId)) {
            throw new UnauthorizedException("접근하신 리소스의 권한이 없습니다");
        }

        review.updateReview(
            request.getContent(),
            request.getImage(),
            request.getRating(),
            request.getSatisfied()
        );

        repository.save(review);
        return mapper.toDto(review);


    }


    public List<Response> getUserReview(Long clientId) {
        List<Review> list = repository.findReviewsByWriter_ClientId(clientId)
            .stream().sorted(Comparator.comparing(Review::getRegisterDate))
            .collect(Collectors.toList());
        return mapper.toDto(list);
    }

    public List<Response> getSellerReview(Long sellerId) {

        return mapper.toDto(repository
            .findReviewsByOrderItem_ItemDetail_Item_Seller_ClientIdOrderByRegisterDate(sellerId));
    }


}
