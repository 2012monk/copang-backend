package com.alconn.copang.address;

import com.alconn.copang.common.EntityPriority;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor @AllArgsConstructor
@Builder
@Getter
public class AddressForm {

    private Long addressId;

    private String addressName;

    @NotBlank(message = "주소는 존재해야합니다")
    private String address;

    @NotBlank(message = "상세 주소는 존재해야합니다")
    private String detail;

    @NotBlank(message = "받는사람 이름은 존재해야 합니다")
    private String receiverName;

    @NotBlank(message = "수령자 연락처는 존재해야 합니다")
    private String receiverPhone;

    private String preRequest;

    @JsonProperty(access = Access.READ_ONLY)
    @Setter
    private Long clientId;

    private EntityPriority priority;

}
