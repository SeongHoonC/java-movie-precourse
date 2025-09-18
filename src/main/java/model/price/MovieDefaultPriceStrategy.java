package model.price;

import model.common.Money;
import model.seat.Seat;
import model.seat.SeatGrade;

class MovieDefaultPriceStrategy implements MoviePriceStrategy {

    @Override
    public Money getPrice(Seat seat) {
        if (seat.seatGrade() == SeatGrade.S) {
            return new Money(18000);
        } else if (seat.seatGrade() == SeatGrade.A) {
            return new Money(15000);
        } else {
            return new Money(12000);
        }
    }
}

