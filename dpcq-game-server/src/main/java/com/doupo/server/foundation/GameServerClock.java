package com.doupo.server.foundation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * 开服日。首充 II/III（30/98）配置条件是 {@code SERVER_OPEN_DAY >= 2}。
 */
@Component
public class GameServerClock {

    private static final ZoneId SERVER_ZONE = ZoneId.of("Asia/Shanghai");

    private final long openTimeMillis;
    private final int openDay;

    @Autowired
    public GameServerClock(
            @Value("${game.server-open-time:}") String openTime) {
        if (openTime == null || openTime.trim().isEmpty()) {
            this.openTimeMillis = 0L;
            this.openDay = 1;
            return;
        }
        this.openTimeMillis = OffsetDateTime.parse(openTime.trim())
                .toInstant()
                .toEpochMilli();
        this.openDay = openDayFrom(this.openTimeMillis, Instant.now());
    }

    GameServerClock(long openTimeMillis, int openDay) {
        this.openTimeMillis = openTimeMillis;
        this.openDay = openDay;
    }

    public static GameServerClock dayOne() {
        return new GameServerClock(0L, 1);
    }

    public long openTimeMillis() {
        return openTimeMillis > 0L ? openTimeMillis : System.currentTimeMillis();
    }

    public int openDay() {
        return openDayAt(Instant.now());
    }

    public int openDayAt(Instant now) {
        return openTimeMillis > 0L ? openDayFrom(openTimeMillis, now) : openDay;
    }

    static int openDayFrom(long openTimeMillis, Instant now) {
        if (openTimeMillis <= 0L) {
            return 1;
        }
        LocalDate open = Instant.ofEpochMilli(openTimeMillis)
                .atZone(SERVER_ZONE)
                .toLocalDate();
        LocalDate today = now.atZone(SERVER_ZONE).toLocalDate();
        long days = ChronoUnit.DAYS.between(open, today) + 1;
        return days < 1L ? 1 : (int) days;
    }
}
