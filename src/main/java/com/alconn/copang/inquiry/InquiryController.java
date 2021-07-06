package com.alconn.copang.inquiry;

import com.alconn.copang.annotations.InjectId;
import com.alconn.copang.client.Role;
import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.UnauthorizedException;
import com.alconn.copang.exceptions.ValidationException;
import com.alconn.copang.inquiry.InquiryForm.Response;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inquiry")
public class InquiryController {

    private final InquiryService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseMessage<InquiryForm.Response> registerInquiry(
        @Validated @RequestBody InquiryForm.Request request,
        @InjectId Long clientId) {
        return ResponseMessage.success(
            service.registerInquiry(request, clientId)
        );
    }

    @PostMapping("/{inquiryId}/reply")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMessage<InquiryForm.Response> registerReply(
        @RequestBody InquiryForm.Request request,
        @InjectId(role = Role.SELLER) Long sellerId,
        @PathVariable(name = "inquiryId") Long inquiryId)
        throws NoSuchEntityExceptions, ValidationException {
        return ResponseMessage.success(
            service.registerReply(request, sellerId, inquiryId)
        );
    }

    @GetMapping("/seller")
    public ResponseMessage<List<Response>> getInquiresBySeller(
        @InjectId(role = Role.SELLER) Long sellerId) {
        return ResponseMessage.success(
            service.getInquiresBySeller(sellerId)
        );
    }

    @GetMapping("/client")
    public ResponseMessage<List<Response>> getInquiresByClient(@InjectId Long clientId) {
        return ResponseMessage.success(
            service.getInquiresByClient(clientId)
        );
    }

    @GetMapping("/{itemId}/item")
    public ResponseMessage<List<Response>> getInquiresByItem(
        @PathVariable(name = "itemId") Long itemId) {
        return ResponseMessage.success(
            service.getInquiresByItem(itemId)
        );
    }

    @PutMapping("/{inquiryId}")
    public ResponseMessage<Response> updateInquiry(@RequestBody InquiryForm.Request request,
        @InjectId Long clientId, @PathVariable Long inquiryId)
        throws NoSuchEntityExceptions, UnauthorizedException {
        return ResponseMessage.success(
            service.updateInquiry(request, inquiryId, clientId)
        );
    }

    @PutMapping("/{inquiryId}/reply")
    public ResponseMessage<Response> updateReply(@RequestBody InquiryForm.Request request,
        @InjectId(role = Role.SELLER) Long sellerId, @PathVariable Long inquiryId)
        throws NoSuchEntityExceptions, UnauthorizedException {
        return ResponseMessage.success(
            service.updateReply(request, inquiryId, sellerId)
        );

    }
}