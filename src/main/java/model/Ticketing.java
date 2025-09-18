package model;

import model.common.Money;
import model.common.Screening;
import model.common.Ticket;
import model.discount.TicketDiscountPolicy;
import model.schedulling.MovieSchedule;
import model.seat.Seat;
import model.seat.SeatGrade;

public class Ticketing {

    final MovieSchedule movieSchedule;
    final TicketDiscountPolicy ticketDiscountPolicy;

    Ticketing(MovieSchedule movieSchedule, TicketDiscountPolicy ticketDiscountPolicy) {
        this.movieSchedule = movieSchedule;
        this.ticketDiscountPolicy = ticketDiscountPolicy;
    }

    public Ticket reserve(
            Screening screening, Seat seat
    ) {
        Money money = calculatorFee(seat);
        var ticket = new Ticket(screening, seat, money);
        return ticketDiscountPolicy.discountTicket(ticket);
    }

    private static Money calculatorFee(Seat seat) {
        if (seat.seatGrade() == SeatGrade.S) {
            return new Money(18000);
        } else if (seat.seatGrade() == SeatGrade.A) {
            return new Money(15000);
        } else {
            return new Money(12000);
        }
    }
}
