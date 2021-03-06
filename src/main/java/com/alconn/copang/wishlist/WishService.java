package com.alconn.copang.wishlist;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.NoSuchUserException;
import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.item.ItemDetailRepository;
import com.alconn.copang.wishlist.dto.WishRequest;
import com.alconn.copang.wishlist.dto.WishResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WishService {

    private final WishRepository wishRepository;

    private final ClientRepo clientRepo;

    private final ItemDetailRepository itemDetailRepository;

    private final WishMapper wishMapper;

    //찜리스트 등록
//    public WithResponse add(WithRequest withRequest) throws NoSuchEntityExceptions {
    //조회하여 있으면 이미 등록된 상품
    @Transactional
    public WishResponse add(WishRequest withRequest,Long clientId) throws NoSuchEntityExceptions {

//        Client client=clientRepo.findById(withRequest.getClientId()).orElseThrow(() -> new NoSuchEntityExceptions("등록된 회원이 아닙니다"));
        System.out.println("clientId = " + clientId);
        Client client=clientRepo.findById(clientId).orElseThrow(NoSuchUserException::new);
        ItemDetail itemDetail=itemDetailRepository.findById(withRequest.getItemDetailId()).orElseThrow(() -> new NoSuchEntityExceptions("등록된 상품이 아닙니다"));

        Wish wish=Wish.builder()
                .itemDetail(itemDetail)
                .client(client)
                .build();
        wishRepository.save(wish);

        WishResponse wishResponse =wishMapper.toRes(wish);
        return wishResponse;
    }

    public List<WishResponse> list(Long clientId) throws NoSuchEntityExceptions {
        //회원인지 체크 후 등록된 정보 출력

//        Client client=clientRepo.findById(wishRequest.getClientId()).orElseThrow(()->new NoSuchElementException("등록된 회원이 아닙니다"));
        Client client=clientRepo.findById(clientId).orElseThrow(NoSuchUserException::new);
        List<Wish> list=wishRepository.findByClient_ClientId(client.getClientId());
        if(list==null) return null;

        List<WishResponse> wishResponseList=wishMapper.toResList(list);
        return wishResponseList;
    }

    //null로 리턴되면 번호입력해달라고 리턴 -> 컨트롤러 단에서 처리
    @Transactional
    public WishResponse del(WishRequest wishRequest, Long clientId) throws NoSuchEntityExceptions{
        if(wishRequest.getWishId()==null) return null;
        try{
            Wish wish=wishRepository.findByClient_ClientIdAndWishId(
                    clientId,
                    wishRequest.getWishId());
            WishResponse wishResponse=wishMapper.toRes(wish);
            wishRepository.deleteById(wish.getWishId());
            return wishResponse;
        }catch (Exception e){
            throw new NoSuchEntityExceptions("요청에 맞는 정보가 없습니다");
        }
    }

    @Transactional
    public List<WishResponse> delList(WishRequest.WishRequestdel wishRequestdel, Long clientId) throws NoSuchEntityExceptions{
        try{
        List<Wish> wishList=wishRepository.findByClient_ClientIdAndWishIdIn(clientId,
                wishRequestdel.getWishId());
        List<WishResponse> wishResponseList=wishMapper.toResList(wishList);

        wishRepository.deleteAll(wishList);
        return wishResponseList;

        }catch (Exception e){
            throw new NoSuchEntityExceptions("요청에 맞는 정보가 없습니다");
        }
    }


}
