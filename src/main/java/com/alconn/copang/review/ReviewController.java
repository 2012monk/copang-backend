package com.alconn.copang.review;

import com.alconn.copang.annotations.InjectId;
import com.alconn.copang.client.Role;
import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.UnauthorizedException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService service;

    @GetMapping("/{itemId}")
    public ResponseMessage<List<ReviewForm.Response>> getReviews(@PathVariable Long itemId) {
        return ResponseMessage.success(
            service.getReviewByItem(itemId)
        );
    }

    // TODO 아이템조회시 주문여부 확인해서 boolean 전달

    @PostMapping("/register")
    public ResponseMessage<ReviewForm.Response> postReview(
        @RequestBody ReviewForm.Request requestForm,
        @InjectId Long clientId) {
        return ResponseMessage.success(
            service.postReview(requestForm, clientId)
        );
    }

    @GetMapping("/user")
    public ResponseMessage<List<ReviewForm.Response>> getUserReview(@InjectId Long clientId) {
        return ResponseMessage.success(
            service.getUserReview(clientId)
        );
    }

    @GetMapping("/seller")
    public ResponseMessage<List<ReviewForm.Response>> getSellerReview(
        @InjectId(role = Role.SELLER) Long sellerId) {
        return ResponseMessage.success(
            service.getSellerReview(sellerId)
        );
    }

    @PutMapping("/{reviewId}")
    public ResponseMessage<ReviewForm.Response> updateReview(
        @InjectId Long clientId,
        @RequestBody ReviewForm.Request request,
        @PathVariable Long reviewId
    ) throws NoSuchEntityExceptions, UnauthorizedException {
        return ResponseMessage.success(
            service.updateReview(request, clientId, reviewId)
        );
    }

}
