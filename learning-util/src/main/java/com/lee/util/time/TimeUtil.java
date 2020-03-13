package com.lee.util.time;

import java.time.*;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

    public static final long defaultOffsetInMillis = TimeZone.getDefault().getRawOffset();
    public static final ZoneOffset defaultOffset;
    static TimeUtil.TimeSource source;

    static {
        defaultOffset = ZoneOffset.ofTotalSeconds((int)TimeUnit.MILLISECONDS.toSeconds(defaultOffsetInMillis));
        source = new TimeUtil.DefaultStandardTimeSource(defaultOffsetInMillis);
    }

    public abstract static class TimeSource {
        public final long timeDisfference;

        public TimeSource(long timeDisfference) {
            this.timeDisfference = timeDisfference;
        }

        public abstract long currentTimeMillis();
    }

    public TimeUtil() {
    }

    public static long currentTimeMillis() {
        return source.currentTimeMillis();
    }

    public static int currentTimeDays() {
        return millisToDays(source.currentTimeMillis());
    }

    private static int millisToDays(long currentTimeMillis) {
        return (int) TimeUnit.MILLISECONDS.toDays(currentTimeMillis + source.timeDisfference);
    }

    public static int daysInWeek() {
        return daysToDaysInWeek((long)currentTimeDays());
    }

    public static int daysToDaysInWeek(long days) {
        return (int)((days - 3L) % 7L);
    }

    public static LocalDate localDate() {
        return localDateTime().toLocalDate();
    }

    private static LocalDateTime localDateTime() {
        return LocalDateTime.ofInstant(instant(),defaultOffset);
    }

    private static Instant instant() {
        return Instant.ofEpochMilli(currentTimeMillis());
    }

    public static void changeTimeDisfference(int hours){
        source = new TimeUtil.DefaultStandardTimeSource(defaultOffsetInMillis + TimeUnit.HOURS.toMillis((long)hours));
    }

    public static void changeCurrentTime(long currentTimeMillis) {
        source = new TimeUtil.HackedTimeSource(source.timeDisfference, currentTimeMillis);
    }

    public static final class HackedTimeSource extends TimeUtil.TimeSource{

        private volatile long currentTimeMillis;
        private volatile long lastExecTime;

        public HackedTimeSource(long timeDisfference) {
            this(timeDisfference,System.currentTimeMillis());
        }

        public HackedTimeSource(long timeDisfference, long currentTimeMillis ) {
            super(timeDisfference);
            this.currentTimeMillis = currentTimeMillis;
            this.lastExecTime = System.currentTimeMillis();
            this.start();
        }

        private void start(){
            Thread t = new Thread(() -> {
                while (!Thread.interrupted()){
                    long now = System.currentTimeMillis();
                    HackedTimeSource.this.currentTimeMillis = HackedTimeSource.this.currentTimeMillis + (now - HackedTimeSource.this.lastExecTime);
                    HackedTimeSource.this.lastExecTime = now;

                    try {
                        TimeUnit.SECONDS.sleep(1L);
                    } catch (InterruptedException var4) {
                    }
                }
            });

            t.setDaemon(true);
            t.start();
        }

        @Override
        public long currentTimeMillis() {
            return this.currentTimeMillis;
        }
    }

    public static final class DefaultStandardTimeSource extends TimeUtil.TimeSource{

        public DefaultStandardTimeSource(long timeDisfference) {
            super(timeDisfference);
        }

        @Override
        public long currentTimeMillis() {
            return System.currentTimeMillis();
        }
    }
}
