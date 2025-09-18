package model.discount;

import model.Money;
import model.Ticket;

import java.time.LocalDate;

public class MovieDayTicketDiscount implements TicketDiscountPolicy {

    @Override
    public Ticket discountTicket(Ticket ticket) {

        boolean discountable = isMovieDay(ticket.screening().start().toLocalDate());

        if (!discountable) {
            return ticket;
        }
        Money price = ticket.price();
        Money discountedPrice = price.minus(price.times(DISCOUNT_RATE));
        return ticket.withPrice(discountedPrice);

    }

    private boolean isMovieDay(LocalDate date) {
        int day = date.getDayOfMonth();
        return day == 10 || day == 20 || day == 30;
    }

    private static final double DISCOUNT_RATE = 0.1;
}
