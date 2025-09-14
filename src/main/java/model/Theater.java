package model;

import model.rules.SeatGradeRule;

import java.util.Map;

public class Theater {

    final Map<SeatNumber, Seat> seats;

    public Theater(Map<SeatNumber, Seat> seats) {
        this.seats = seats;
    }

    public static Theater of(String lastRow, int lastColumn, SeatGradeRule seatGradeRule) {

        Map<SeatNumber, Seat> seats = new java.util.HashMap<>();

        for (char row = 'A'; row <= lastRow.charAt(0); row++) {
            for (int column = 1; column <= lastColumn; column++) {
                SeatNumber seatNumber = new SeatNumber(String.valueOf(row), column);
                SeatGrade seatGrade = seatGradeRule.getSeatGrade(seatNumber);
                Seat seat = new Seat(seatNumber, seatGrade);
                seats.put(seatNumber, seat);
            }
        }
        return new Theater(seats);
    }
}
