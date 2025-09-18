package model.common;

import model.seat.Seat;

public record ScreeningSeat(
        Screening screening,
        Seat seat
) {
}
