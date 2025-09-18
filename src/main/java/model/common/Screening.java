package model.common;

import java.time.LocalDateTime;

public record Screening(
        Integer id,
        Movie movie,
        LocalDateTime start,
        LocalDateTime end,
        Auditorium auditorium
) {
}
