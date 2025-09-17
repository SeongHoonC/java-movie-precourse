package model.seat;

public record Seat(
        SeatNumber seatNumber,
        SeatGrade seatGrade,
        boolean isReserved
) {

    public Seat(SeatNumber seatNumber, SeatGrade seatGrade) {
        this(seatNumber, seatGrade, false);
    }
}
