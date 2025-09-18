package model.common;

import java.util.List;

public record TicketingResult(
        List<Ticket> tickets,
        Money totalPrice
) {
}


