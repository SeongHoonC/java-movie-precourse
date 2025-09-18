package model.common;

import model.seat.SeatGradeRule;
import model.seat.Seat;
import model.seat.SeatGrade;
import model.seat.SeatNumber;

import java.util.Map;

public class Auditorium {

    final Map<SeatNumber, Seat> seats;

    public Auditorium(Map<SeatNumber, Seat> seats) {
        this.seats = seats;
    }

    public static Auditorium of(char lastRow, int lastColumn, SeatGradeRule seatGradeRule) {

        Map<SeatNumber, Seat> seats = new java.util.HashMap<>();

        for (char row = 'A'; row <= lastRow; row++) {
            for (int column = 1; column <= lastColumn; column++) {
                SeatNumber seatNumber = new SeatNumber(row, column);
                SeatGrade seatGrade = seatGradeRule.getSeatGrade(seatNumber);
                Seat seat = new Seat(seatNumber, seatGrade);
                seats.put(seatNumber, seat);
            }
        }
        return new Auditorium(seats);
    }
}
