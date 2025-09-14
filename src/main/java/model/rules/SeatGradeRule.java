package model.rules;

import model.SeatGrade;
import model.SeatNumber;

public interface SeatGradeRule {
    SeatGrade getSeatGrade(SeatNumber seatNumber);
}
