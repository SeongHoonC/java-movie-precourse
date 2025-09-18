package model;

import model.common.Money;
import model.common.Screening;
import model.common.Ticket;
import model.discount.TicketDiscountPolicy;
import model.price.MoviePriceStrategy;
import model.schedulling.MovieSchedule;
import model.seat.Seat;

public class Ticketing {

    final MovieSchedule movieSchedule;
    final TicketDiscountPolicy ticketDiscountPolicy;
    final MoviePriceStrategy moviePriceStrategy;

    Ticketing(MovieSchedule movieSchedule,
              TicketDiscountPolicy ticketDiscountPolicy,
              MoviePriceStrategy moviePriceStrategy) {
        this.movieSchedule = movieSchedule;
        this.ticketDiscountPolicy = ticketDiscountPolicy;
        this.moviePriceStrategy = moviePriceStrategy;
    }

    public Ticket reserve(
            Screening screening, Seat seat
    ) {
        Money price = moviePriceStrategy.getPrice(seat);
        var ticket = new Ticket(screening, seat, price);
        return ticketDiscountPolicy.discountTicket(ticket);
    }
}
