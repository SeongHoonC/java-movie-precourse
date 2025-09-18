package model.discount;

import model.*;
import model.seat.Seat;
import model.seat.SeatGrade;
import model.seat.SeatNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class TimeBasedMovieDiscountTest {

    private TimeBasedMovieDiscount timeBasedMovieDiscount;
    private Movie movie;
    private Auditorium auditorium;
    private Seat seat;

    @BeforeEach
    void setUp() {
        timeBasedMovieDiscount = new TimeBasedMovieDiscount();
        movie = new Movie(1, "테스트 영화", 120);
        
        // Auditorium 생성
        Map<SeatNumber, Seat> seats = new HashMap<>();
        SeatNumber seatNumber = new SeatNumber('A', 1);
        seat = new Seat(seatNumber, SeatGrade.A);
        seats.put(seatNumber, seat);
        auditorium = new Auditorium(seats);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    @DisplayName("오전 11시 이전 상영에는 2000원 할인이 적용된다")
    void discountTicketOnDiscountHours1(int hour) {
        // given
        LocalDateTime screeningTime = LocalDateTime.of(2024, 1, 15, hour, 30);
        Screening screening = new Screening(1, movie, screeningTime, screeningTime.plusMinutes(120), auditorium);
        Money originalPrice = new Money(10000);
        Ticket ticket = new Ticket(screening, seat, originalPrice);

        // when
        Ticket discountedTicket = timeBasedMovieDiscount.discountTicket(ticket);

        // then
        assertThat(discountedTicket.price().amount()).isEqualTo(8000);
    }


    @ParameterizedTest
    @ValueSource(ints = {20, 21, 22, 23})
    @DisplayName("오후 8시 이후 상영에는 2000원 할인이 적용된다")
    void discountTicketOnDiscountHours2(int hour) {
        // given
        LocalDateTime screeningTime = LocalDateTime.of(2024, 1, 15, hour, 30);
        Screening screening = new Screening(1, movie, screeningTime, screeningTime.plusMinutes(120), auditorium);
        Money originalPrice = new Money(10000);
        Ticket ticket = new Ticket(screening, seat, originalPrice);

        // when
        Ticket discountedTicket = timeBasedMovieDiscount.discountTicket(ticket);

        // then
        assertThat(discountedTicket.price().amount()).isEqualTo(8000);
    }

    @ParameterizedTest
    @ValueSource(ints = {12, 13, 14, 15, 16, 17, 18, 19})
    @DisplayName("할인 시간대가 아닌 시간(11시-19시)에는 할인이 적용되지 않는다")
    void noDiscountOnRegularHours(int hour) {
        // given
        LocalDateTime screeningTime = LocalDateTime.of(2024, 1, 15, hour, 30);
        Screening screening = new Screening(1, movie, screeningTime, screeningTime.plusMinutes(120), auditorium);
        Money originalPrice = new Money(10000);
        Ticket ticket = new Ticket(screening, seat, originalPrice);

        // when
        Ticket resultTicket = timeBasedMovieDiscount.discountTicket(ticket);

        // then
        assertThat(resultTicket.price().amount()).isEqualTo(10000);
        assertThat(resultTicket).isEqualTo(ticket); // 원본 티켓과 동일
    }

    @Test
    @DisplayName("정확히 11시에는 할인된다")
    void noDiscountAtExactly11AM() {
        // given
        LocalDateTime exactly11AM = LocalDateTime.of(2024, 1, 15, 11, 0);
        Screening screening = new Screening(1, movie, exactly11AM, exactly11AM.plusMinutes(120), auditorium);
        Money originalPrice = new Money(10000);
        Ticket ticket = new Ticket(screening, seat, originalPrice);

        // when
        Ticket discountedTicket = timeBasedMovieDiscount.discountTicket(ticket);

        // then
        assertThat(discountedTicket.price().amount()).isEqualTo(8000);
    }

    @Test
    @DisplayName("정확히 20시에는 할인이 적용된다")
    void discountAtExactly8PM() {
        // given
        LocalDateTime exactly8PM = LocalDateTime.of(2024, 1, 15, 20, 0);
        Screening screening = new Screening(1, movie, exactly8PM, exactly8PM.plusMinutes(120), auditorium);
        Money originalPrice = new Money(10000);
        Ticket ticket = new Ticket(screening, seat, originalPrice);

        // when
        Ticket discountedTicket = timeBasedMovieDiscount.discountTicket(ticket);

        // then
        assertThat(discountedTicket.price().amount()).isEqualTo(8000);
    }
}
