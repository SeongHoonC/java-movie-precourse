package model.common;

import java.time.LocalDateTime;

public record OperatingTimes(
        LocalDateTime openTime,
        LocalDateTime closeTime
) {
}