package com.alconn.copang.common;

import com.alconn.copang.client.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class AccessTokenContainer {

    private final String access_token;

    private final String username;

    private final Role role;
}
