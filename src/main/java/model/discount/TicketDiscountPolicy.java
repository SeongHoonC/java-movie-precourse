package model.discount;

import model.common.Ticket;

public interface TicketDiscountPolicy {
    public Ticket discountTicket(Ticket ticket);
}
