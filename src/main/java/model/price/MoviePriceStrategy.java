package model.price;

import model.common.Money;
import model.seat.Seat;

public interface MoviePriceStrategy {
    Money getPrice(Seat seat);
}