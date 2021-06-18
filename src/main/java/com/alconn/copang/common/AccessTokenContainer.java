package com.alconn.copang.common;

import com.alconn.copang.client.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class AccessTokenContainer {

    private String access_token;

    private String username;

    private Role role;
}
