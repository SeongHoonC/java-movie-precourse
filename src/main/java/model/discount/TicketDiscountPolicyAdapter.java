package model.discount;

import model.common.Ticket;

import java.util.List;

public class TicketDiscountPolicyAdapter implements TicketDiscountPolicy {

    private final List<TicketDiscountPolicy> ticketDiscountPolices;

    public TicketDiscountPolicyAdapter(List<TicketDiscountPolicy> ticketDiscountPolices) {
        this.ticketDiscountPolices = ticketDiscountPolices;
    }

    @Override
    public Ticket discountTicket(Ticket ticket) {

        for (TicketDiscountPolicy policy : ticketDiscountPolices) {
            ticket = policy.discountTicket(ticket);
        }

        return ticket;
    }
}
