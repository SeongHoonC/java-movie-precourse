package model.price;

import model.common.Money;
import model.seat.Seat;
import model.seat.SeatGrade;

public class MovieDefaultPriceStrategy implements MoviePriceStrategy {

    @Override
    public Money getPrice(Seat seat) {
        SeatGrade grade = seat.seatGrade();
        if (grade == SeatGrade.S) return new Money(18000);
        if (grade == SeatGrade.A) return new Money(15000);
        return new Money(12000);
    }
}

