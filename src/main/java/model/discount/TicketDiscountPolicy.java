package model.discount;

import model.Ticket;

public interface TicketDiscountPolicy {
    public Ticket discountTicket(Ticket ticket);
}
