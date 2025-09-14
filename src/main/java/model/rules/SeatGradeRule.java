package model.rules;

import model.seat.SeatGrade;
import model.seat.SeatNumber;

public interface SeatGradeRule {
    SeatGrade getSeatGrade(SeatNumber seatNumber);
}
