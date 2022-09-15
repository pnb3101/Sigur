package util;

import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class TimeConstants {
    public static final OffsetDateTime START_TIME = OffsetDateTime.of(
            2022, Month.JANUARY.getValue(), 1, 0, 0, 0, 1, ZoneOffset.UTC);
    public static final OffsetDateTime END_TIME = OffsetDateTime.of(
            2023, Month.JANUARY.getValue(), 1, 0, 0, 0, 1, ZoneOffset.UTC);
}
