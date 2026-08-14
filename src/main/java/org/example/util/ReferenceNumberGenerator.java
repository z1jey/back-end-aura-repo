package org.example.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

public final class ReferenceNumberGenerator {

    private static final String PREFIX = "TXN";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private static final AtomicLong SEQUENCE = new AtomicLong(0L);

    private ReferenceNumberGenerator() {
    }

    public static String generate() {
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        long seq = SEQUENCE.incrementAndGet();
        return String.format("%s%s%09d", PREFIX, timestamp, seq);  // 9-digit zero-padded sequence
    }
}
