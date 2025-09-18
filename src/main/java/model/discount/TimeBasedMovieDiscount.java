package model.discount;

import model.Money;
import model.Ticket;

import java.time.LocalTime;

public class TimeBasedMovieDiscount implements TicketDiscountPolicy {

    @Override
    public Ticket discountTicket(Ticket ticket) {
        boolean discountable = isEarlyBird(ticket.screening().start().toLocalTime());
        if (!discountable) {
            return ticket;
        }
        Money price = ticket.price();
        Money discountedPrice = price.minus(new Money(DISCOUNT_AMOUNT));
        return ticket.withPrice(discountedPrice);

    }

    private boolean isEarlyBird(LocalTime dateTime) {
        int hour = dateTime.getHour();
        return hour <= 11 || hour >= 20;
    }

    static final int DISCOUNT_AMOUNT = 2_000;
}
