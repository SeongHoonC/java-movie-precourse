package model;

import java.time.LocalDateTime;

public record Screening(
        int id,
        Movie movie,
        LocalDateTime start,
        LocalDateTime end,
        Auditorium auditorium
) {
}
