package model;

import model.seat.Seat;

public record Ticket(Screening screening, Seat seat, Money price) {
    public Ticket withPrice(Money newPrice) {
        return new Ticket(this.screening, this.seat, newPrice);
    }
}
