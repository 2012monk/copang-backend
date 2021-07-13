package com.alconn.copang;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

public class TiemTest {

    @Test
    void name() {
        long epoch = 1625618181L;
        LocalDateTime localDateTime = LocalDateTime
            .ofInstant(Instant.ofEpochMilli(epoch), ZoneOffset.UTC);
        System.out.println("localDateTime.toString() = " + localDateTime.toString());
        System.out.println("System.currentTimeMillis() = " + System.currentTimeMillis());
    }
}
