package model.rules;

import model.seat.SeatGrade;
import model.seat.SeatNumber;

import java.util.List;


public class DefaultSeatGradeRule implements SeatGradeRule {

    public SeatGrade getSeatGrade(SeatNumber seatNumber) {
        if (List.of("E", "F", "G", "H").contains(seatNumber.row())) {
            return SeatGrade.S;
        }

        if (List.of("A", "B", "C", "D").contains(seatNumber.row())) {
            return SeatGrade.B;
        }

        return SeatGrade.A;
    }
}
