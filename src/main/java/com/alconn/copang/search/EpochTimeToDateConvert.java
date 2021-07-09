package com.alconn.copang.search;

import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.util.StdConverter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class EpochTimeToDateConvert extends StdConverter<Long, LocalDate> {

    @Override
    public LocalDate convert(Long value) {
        return Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
