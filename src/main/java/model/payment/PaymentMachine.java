package model.payment;

import model.common.Money;
import model.common.TicketBunch;

public class PaymentMachine {

    public Money calculatePayable(TicketBunch ticketBunch, Money point, PaymentMethod paymentMethod) {
        Money total = ticketBunch.totalPrice();
        int pointsToApply = Math.min(point.amount(), total.amount());
        Money afterPoints = total.minus(new Money(pointsToApply));

        double discountRate = switch (paymentMethod) {
            case CARD -> 0.05;
            case CASH -> 0.02;
        };

        Money methodDiscount = afterPoints.times(discountRate);
        return afterPoints.minus(methodDiscount);
    }
}


