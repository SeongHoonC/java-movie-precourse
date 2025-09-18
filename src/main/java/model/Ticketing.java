package model;

import model.common.Money;
import model.common.Screening;
import model.common.Ticket;
import model.common.TicketBunch;
import model.common.ScreeningSeat;
import model.discount.TicketDiscountPolicy;
import model.price.MoviePriceStrategy;
import model.schedulling.MovieSchedule;
import model.seat.Seat;

import java.util.List;

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

    public Ticket reserve(Screening screening, Seat seat) {
        if (seat.isReserved()) {
            throw new IllegalArgumentException("이미 예약된 좌석입니다.");
        }
        var ticket = new Ticket(screening, seat, moviePriceStrategy.getPrice(seat));
        return ticketDiscountPolicy.discountTicket(ticket);
    }

    public TicketBunch reserveAll(java.util.List<ScreeningSeat> items) {
        for (int i = 0; i < items.size(); i++) {
            for (int j = i + 1; j < items.size(); j++) {
                if (isOverlapped(items.get(i).screening(), items.get(j).screening())) {
                    throw new IllegalArgumentException("겹치는 상영 시간이 포함되어 있습니다.");
                }
            }
        }

        List<Ticket> tickets = items.stream()
                .map(item -> reserve(item.screening(), item.seat()))
                .toList();

        return new TicketBunch(tickets);
    }

    private boolean isOverlapped(Screening a, Screening b) {
        return !(a.end().isEqual(b.start()) || a.end().isBefore(b.start()) ||
                b.end().isEqual(a.start()) || b.end().isBefore(a.start()));
    }
}
