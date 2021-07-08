package com.alconn.copang.inquiry;

import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.UnauthorizedException;
import com.alconn.copang.exceptions.ValidationException;
import com.alconn.copang.inquiry.InquiryForm.Request;
import com.alconn.copang.inquiry.InquiryForm.Response;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository repository;

    private final InquiryMapper mapper;

    @Transactional
    public InquiryForm.Response registerInquiry(InquiryForm.Request requestForm, Long clientId) {
        Inquiry inquiry = mapper.toEntity(requestForm, clientId);

        repository.save(inquiry);

        return mapper.toDto(inquiry);
    }

    @Transactional
    public InquiryForm.Response registerReply(Request requestForm, Long sellerId,
        Long inquiryId) throws NoSuchEntityExceptions, ValidationException {

        Inquiry inquiry = repository.findById(inquiryId).orElseThrow(() -> new NoSuchEntityExceptions("요청하신 문의가 없습니다"));

        if (inquiry.getReply() != null) {
            throw new ValidationException("이미 답변이 등록되어 있습니다");
        }

        Reply reply = mapper.toReply(requestForm, sellerId);
        inquiry.reply(reply);
        repository.save(inquiry);


        return mapper.toDto(inquiry);
    }



    @Transactional
    public InquiryForm.Response updateInquiry(Request request, Long inquiryId,
        Long clientId) throws NoSuchEntityExceptions, UnauthorizedException {

        Inquiry inquiry = repository.findById(inquiryId).orElseThrow(NoSuchEntityExceptions::new);
        if (!inquiry.getClient().getClientId().equals(clientId)) {
            throw new UnauthorizedException("본인인증에 실패했습니다");
        }
        inquiry.updateContent(request.getContent());
        return mapper.toDto(inquiry);
    }

    @Transactional
    public InquiryForm.Response updateReply(Request request, Long inquiryId, Long sellerId)
        throws NoSuchEntityExceptions, UnauthorizedException {
        Inquiry inquiry = repository.findById(inquiryId).orElseThrow(NoSuchEntityExceptions::new);
        if (!inquiry.getReply().getSeller().getClientId().equals(sellerId)) {
            throw new UnauthorizedException("본인인증에 실패했습니다");
        }

        inquiry.getReply().updateContent(request.getContent());
        return mapper.toDto(inquiry);
    }


    public List<Response> getInquiresByClient(Long clientId) {
        return repository.findInquiriesByClient_ClientId(clientId)
            .stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<InquiryForm.Response> getInquiresBySeller(Long sellerId) {
        List<Inquiry> inquiries = repository
            .findInquiriesByItemDetail_Item_Seller_ClientId(sellerId);

        return inquiries.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<InquiryForm.Response> getInquiresByItem(Long itemId) {

        return repository.findInquiriesByItemDetail_Item_ItemId(itemId)
            .stream().map(mapper::toDto).collect(Collectors.toList());
    }
}
