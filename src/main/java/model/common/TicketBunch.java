package model.common;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class TicketBunch {

    private final Set<Ticket> tickets;

    public TicketBunch(List<Ticket> tickets) {
        // 중복 검사
        Set<Ticket> ticketSet = new HashSet<>(tickets);
        if (ticketSet.size() != tickets.size()) {
            throw new IllegalArgumentException("중복된 티켓이 포함되어 있습니다.");
        }
        this.tickets = Collections.unmodifiableSet(ticketSet);
    }

    public Set<Ticket> tickets() {
        return tickets;
    }

    public Money totalPrice() {
        return tickets.stream()
                .map(Ticket::price)
                .reduce(Money::plus)
                .orElseThrow(() -> new IllegalStateException("No tickets"));
    }
}
