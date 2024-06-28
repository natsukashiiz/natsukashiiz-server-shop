package com.natsukashiiz.shop.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class TimeUtils {

    public static LocalDateTime fromUnix(long unix) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(unix), TimeZone.getDefault().toZoneId());
    }

    public static Long toMilli(LocalDateTime time) {
        ZonedDateTime zdt = ZonedDateTime.of(time, ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }
}
