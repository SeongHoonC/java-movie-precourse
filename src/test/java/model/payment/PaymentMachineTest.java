package model.payment;

import model.common.Money;
import model.common.Ticket;
import model.common.TicketBunch;
import model.common.Screening;
import model.common.Auditorium;
import model.seat.DefaultSeatGradeRule;
import model.seat.Seat;
import model.seat.SeatGrade;
import model.seat.SeatNumber;
import model.common.Movie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentMachineTest {

    @Test
    @DisplayName("포인트 선차감 후 결제수단 할인 적용 - 카드 5%")
    void calculatePayableWithCard() {
        PaymentMachine machine = new PaymentMachine();

        Auditorium auditorium = Auditorium.of('A', 1, new DefaultSeatGradeRule());
        Screening screening = new Screening(1, new Movie(1, "M1", 120), now(10, 0), now(12, 0), auditorium);
        Money point = new Money(2000);

        Ticket t1 = new Ticket(screening, new Seat(new SeatNumber('A', 1), SeatGrade.A), new Money(10000));
        Ticket t2 = new Ticket(screening, new Seat(new SeatNumber('A', 2), SeatGrade.A), new Money(10000));

        TicketBunch bunch = new TicketBunch(List.of(t1, t2));

        Money payable = machine.calculatePayable(bunch, point, PaymentMethod.CARD);

        assertThat(payable).isEqualTo(new Money(17100));
    }

    @Test
    @DisplayName("포인트 선차감 후 결제수단 할인 적용 - 현금 2%")
    void calculatePayableWithCash() {
        PaymentMachine machine = new PaymentMachine();
        Auditorium auditorium = Auditorium.of('A', 1, new DefaultSeatGradeRule());
        Screening screening = new Screening(1, new Movie(1, "M1", 120), now(10, 0), now(12, 0), auditorium);

        Seat seat = new Seat(new SeatNumber('A', 1), SeatGrade.A);
        Ticket t1 = new Ticket(screening, seat, new Money(10000));
        Money point = new Money(1000);
        TicketBunch bunch = new TicketBunch(List.of(t1));

        Money payable = machine.calculatePayable(bunch, point, PaymentMethod.CASH);

        // (10000 - 1000) * 0.98 = 8820
        assertThat(payable).isEqualTo(new Money(8820));
    }

    private static LocalDateTime now(int hour, int minute) {
        return LocalDateTime.of(2024, 1, 10, hour, minute);
    }
}


