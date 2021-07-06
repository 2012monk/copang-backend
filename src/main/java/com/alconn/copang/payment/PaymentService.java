package com.alconn.copang.payment;

import com.alconn.copang.inquiry.InquiryForm.Response;
import com.alconn.copang.payment.PaymentForm.Request;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Transactional
@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${spring.imp.key}")
    private String impKey;

    @Value("${spring.imp.secret}")
    private String impSecret;

    @Value("${spring.imp.api-url}")
    private String baseUrl;

    private String accessToken;

    private Integer exp;

    private final RestTemplate restTemplate;

    @PostConstruct
    private void init(){
        if (accessToken == null) {
            getImpToken();
        }
        else if(exp != null && exp < new Date().getTime()) {
            getImpToken();
        }
    }

    public PaymentForm.Prepare preparePayment(Request request, Long clientId) {
        return null;
    }
    /**
     * 결제금액과 주문금액이 일치하는지 결제가 완료되었는지 검증
     *
     * @param impUid Iamport 결제번호
     * @return 검증여부
     */
    public PaymentForm validatePayment(String impUid, String orderId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", this.accessToken);
        headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
        String requestUri = baseUrl + "/payments/" + impUid;

        HttpEntity<HttpHeaders> req = new HttpEntity<>(headers);
        ResponseEntity<ImpResponse> res = restTemplate.exchange(
            requestUri,
            HttpMethod.GET,
            req,
            ImpResponse.class
        );
        
        Map<String, Objects> map = (Map<String, Objects>) Objects.requireNonNull(res.getBody()).getResponse();

        System.out.println("map.get(\"buyer_addr\") = " + map.get("buyer_addr"));

        System.out.println("res.getBody() = " + res.getBody());

        return null;
    }

    public String getImpToken() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        LinkedMultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>() {{
            add("imp_key", impKey);
            add("imp_secret", impSecret);
        }};

        HttpEntity<MultiValueMap<String, String >> req = new HttpEntity<>(param, httpHeaders);
        ResponseEntity<Map> res = restTemplate
            .postForEntity(baseUrl + "/users/getToken", req, Map.class);

//        ImpResponse<ImpTokenContainer> resType = new ImpResponse<>();
//        Type type = ((ParameterizedType) resType.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//        ResponseEntity<ImpResponse<ImpTokenContainer>> res = restTemplate
//            .postForEntity(baseUrl + "users/getToken", req, (Class<ImpResponse<ImpTokenContainer>>) type );

//        ImpTokenContainer body = (ImpTokenContainer) Objects.requireNonNull(res.getBody()).getResponse();
        Map response = (Map) res.getBody().get("response");
        String token = (String) response.get("access_token");
        Integer exp = (Integer) response.get("expired_at");
        System.out.println("res.getBody() = " + res.getBody());

        this.accessToken = token;
        this.exp = exp;
        return token;

    }


}
