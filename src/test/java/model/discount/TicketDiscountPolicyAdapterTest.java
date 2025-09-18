package model.discount;

import model.*;
import model.rules.DefaultSeatGradeRule;
import model.seat.Seat;
import model.seat.SeatGrade;
import model.seat.SeatNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TicketDiscountPolicyAdapterTest {

    private Movie movie;
    private Seat seat;
    private Auditorium auditorium;

    private class FakeTicketDiscountPolicy implements TicketDiscountPolicy {

        private final Money discountAmount;

        public FakeTicketDiscountPolicy(Money discountAmount) {
            this.discountAmount = discountAmount;
        }

        @Override
        public Ticket discountTicket(Ticket ticket) {
            Money discountedPrice = ticket.price().minus(discountAmount);
            return ticket.withPrice(discountedPrice);
        }
    }

    @BeforeEach
    void setUp() {
        movie = new Movie(1, "테스트 영화", 120);
        seat = new Seat(new SeatNumber('A', 1), SeatGrade.A);
        auditorium = Auditorium.of('F', 10, new DefaultSeatGradeRule());
    }

    @Test
    @DisplayName("여러 할인 정책을 순차적으로 적용한다")
    void applyMultipleDiscountPolicies() {
        // given
        LocalDateTime screeningTime = LocalDateTime.of(2024, 1, 15, 14, 0);
        Screening screening = new Screening(1, movie, screeningTime, screeningTime.plusMinutes(120), auditorium);
        Money originalPrice = new Money(10000);
        Ticket ticket = new Ticket(screening, seat, originalPrice);

        FakeTicketDiscountPolicy fakePolicy1 = new FakeTicketDiscountPolicy(new Money(1000));
        FakeTicketDiscountPolicy fakePolicy2 = new FakeTicketDiscountPolicy(new Money(2000));
        
        List<TicketDiscountPolicy> policies = List.of(fakePolicy1, fakePolicy2);
        TicketDiscountPolicyAdapter adapter = new TicketDiscountPolicyAdapter(policies);

        // when
        Ticket discountedTicket = adapter.discountTicket(ticket);

        // then
        assertThat(discountedTicket.price().amount()).isEqualTo(7000);
    }

    @Test
    @DisplayName("500원, 1000원, 1500원 할인 테스트")
    void verifyPolicyCallOrder() {
        // given
        LocalDateTime screeningTime = LocalDateTime.of(2024, 1, 15, 14, 0);
        Screening screening = new Screening(1, movie, screeningTime, screeningTime.plusMinutes(120), auditorium);
        Money originalPrice = new Money(10000);
        Ticket ticket = new Ticket(screening, seat, originalPrice);

        FakeTicketDiscountPolicy fakePolicy1 = new FakeTicketDiscountPolicy(new Money(500));
        FakeTicketDiscountPolicy fakePolicy2 = new FakeTicketDiscountPolicy(new Money(1000));
        FakeTicketDiscountPolicy fakePolicy3 = new FakeTicketDiscountPolicy(new Money(1500));

        List<TicketDiscountPolicy> policies = List.of(fakePolicy1, fakePolicy2, fakePolicy3);
        TicketDiscountPolicyAdapter adapter = new TicketDiscountPolicyAdapter(policies);

        // when
        Ticket discountedTicket = adapter.discountTicket(ticket);

        // then
        assertThat(discountedTicket.price().amount()).isEqualTo(7000);
    }
}
