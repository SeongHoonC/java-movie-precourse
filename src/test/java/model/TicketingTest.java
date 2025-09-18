package model;

import model.common.*;
import model.discount.TicketDiscountPolicy;
import model.price.MoviePriceStrategy;
import model.seat.DefaultSeatGradeRule;
import model.seat.Seat;
import model.seat.SeatGrade;
import model.seat.SeatNumber;
import model.schedulling.MovieSchedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TicketingTest {

    private static class NoDiscountPolicy implements TicketDiscountPolicy {
        @Override
        public model.common.Ticket discountTicket(model.common.Ticket ticket) {
            return ticket;
        }
    }

    private static class FixedPriceStrategy implements MoviePriceStrategy {
        private final Money price;
        FixedPriceStrategy(int price) { this.price = new Money(price); }
        @Override
        public Money getPrice(Seat seat) { return price; }
    }

    @Test
    @DisplayName("여러 상영/좌석을 한 번에 예매하고 총액을 계산한다")
    void reserveAllSuccess() {
        MovieSchedule schedule = new MovieSchedule();
        Ticketing ticketing = new Ticketing(schedule, new NoDiscountPolicy(), new FixedPriceStrategy(10000));

        Auditorium auditorium = Auditorium.of('B', 2, new DefaultSeatGradeRule());
        Seat seatA1 = new Seat(new SeatNumber('A', 1), SeatGrade.A);
        Seat seatA2 = new Seat(new SeatNumber('A', 2), SeatGrade.A);

        LocalDateTime s1 = LocalDateTime.of(2024,1,10,10,0);
        LocalDateTime e1 = s1.plusMinutes(120);
        LocalDateTime s2 = LocalDateTime.of(2024,1,10,13,0);
        LocalDateTime e2 = s2.plusMinutes(120);

        Screening screening1 = new Screening(1, new Movie(1, "M1", 120), s1, e1, auditorium);
        Screening screening2 = new Screening(2, new Movie(2, "M2", 120), s2, e2, auditorium);

        TicketBunch result = ticketing.reserveAll(List.of(
                new ScreeningSeat(screening1, seatA1),
                new ScreeningSeat(screening2, seatA2)
        ));

        assertThat(result.tickets()).hasSize(2);
        assertThat(result.totalPrice()).isEqualTo(new Money(20000));
    }

    @Test
    @DisplayName("이미 예약된 좌석이 포함되면 예외")
    void reserveAllFailsWhenSeatAlreadyReserved() {
        MovieSchedule schedule = new MovieSchedule();
        Ticketing ticketing = new Ticketing(schedule, new NoDiscountPolicy(), new FixedPriceStrategy(10000));

        Auditorium auditorium = Auditorium.of('A', 2, new DefaultSeatGradeRule());
        Seat reservedSeat = new Seat(new SeatNumber('A', 1), SeatGrade.A, true);
        Seat seatA2 = new Seat(new SeatNumber('A', 2), SeatGrade.A);

        LocalDateTime s1 = LocalDateTime.of(2024,1,10,10,0);
        LocalDateTime e1 = s1.plusMinutes(120);
        LocalDateTime s2 = LocalDateTime.of(2024,1,10,13,0);
        LocalDateTime e2 = s2.plusMinutes(120);

        Screening screening1 = new Screening(1, new Movie(1, "M1", 120), s1, e1, auditorium);
        Screening screening2 = new Screening(2, new Movie(2, "M2", 120), s2, e2, auditorium);

        assertThatThrownBy(() -> ticketing.reserveAll(List.of(
                        new ScreeningSeat(screening1, reservedSeat),
                        new ScreeningSeat(screening2, seatA2)
                )))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 예약된 좌석입니다.");
    }

    @Test
    @DisplayName("상영 시간이 겹치면 예외")
    void reserveAllFailsWhenOverlapped() {
        MovieSchedule schedule = new MovieSchedule();
        Ticketing ticketing = new Ticketing(schedule, new NoDiscountPolicy(), new FixedPriceStrategy(10000));

        Auditorium auditorium = Auditorium.of('A', 2, new DefaultSeatGradeRule());
        Seat seatA1 = new Seat(new SeatNumber('A', 1), SeatGrade.A);
        Seat seatA2 = new Seat(new SeatNumber('A', 2), SeatGrade.A);

        LocalDateTime s1 = LocalDateTime.of(2024,1,10,10,0);
        LocalDateTime e1 = s1.plusMinutes(120);
        LocalDateTime s2 = LocalDateTime.of(2024,1,10,11,0); // 겹침
        LocalDateTime e2 = s2.plusMinutes(120);

        Screening screening1 = new Screening(1, new Movie(1, "M1", 120), s1, e1, auditorium);
        Screening screening2 = new Screening(2, new Movie(2, "M2", 120), s2, e2, auditorium);

        assertThatThrownBy(() -> ticketing.reserveAll(List.of(
                        new ScreeningSeat(screening1, seatA1),
                        new ScreeningSeat(screening2, seatA2)
                )))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("겹치는 상영 시간");
    }
}


