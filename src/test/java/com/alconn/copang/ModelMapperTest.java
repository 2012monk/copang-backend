package com.alconn.copang;

import com.alconn.copang.auth.LoginToken;
import com.alconn.copang.client.Client;
import com.alconn.copang.client.UserForm;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

@Disabled
public class ModelMapperTest {

    ModelMapper mapper = new ModelMapper();

    @Test
    void name() {
        mapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
        .setFieldMatchingEnabled(true);
        LoginToken token = new LoginToken();
        ReflectionTestUtils.setField(token, "username", "name");
        ReflectionTestUtils.setField(token, "password", "pass");

        System.out.println(token.getUsername());
        Client client = mapper.map(token, Client.class);
        LoginToken token1 = new LoginToken();

        ReflectionTestUtils.setField(token1, "username","name1`123");
        System.out.println(token1.getUsername());
        assert client != null;
        System.out.println(client.getUsername());
        System.out.println(client.getPassword());
        // Token이 변경됨
        mapper.map(token1, token);
        System.out.println(token.getUsername()+token1.getUsername());
        System.out.println("token1.getPassword()+token.getPassword() = " + token1.getPassword()+token.getPassword());
//        assert client.getUsername().equals(token.getUsername());

    }
}
