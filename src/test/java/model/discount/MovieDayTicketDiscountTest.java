package model.discount;

import model.common.*;
import model.seat.DefaultSeatGradeRule;
import model.seat.Seat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MovieDayTicketDiscountTest {

    private MovieDayTicketDiscount movieDayTicketDiscount;
    private Movie movie;
    private Auditorium auditorium;
    private Seat seat;

    @BeforeEach
    void setUp() {
        movieDayTicketDiscount = new MovieDayTicketDiscount();
        movie = new Movie(1, "테스트 영화", 120);
        auditorium = Auditorium.of('F', 10, new DefaultSeatGradeRule());

    }

    @ParameterizedTest
    @ValueSource(ints = {10, 20, 30})
    @DisplayName("영화의 날(10일, 20일, 30일)에는 10% 할인이 적용된다")
    void discountTicketOnVariousMovieDays(int day) {
        // given
        LocalDateTime movieDay = LocalDateTime.of(2024, 1, day, 14, 0);
        Screening screening = new Screening(1, movie, movieDay, movieDay.plusMinutes(120), auditorium);
        Money originalPrice = new Money(10000);
        Ticket ticket = new Ticket(screening, seat, originalPrice);

        // when
        Ticket discountedTicket = movieDayTicketDiscount.discountTicket(ticket);

        // then
        Money expected = new Money(9000);
        assertThat(discountedTicket.price()).isEqualTo(expected);
    }

    @Test
    @DisplayName("영화의 날이 아닌 날에는 할인이 적용되지 않는다")
    void noDiscountOnNonMovieDay() {
        // given
        LocalDateTime nonMovieDay = LocalDateTime.of(2024, 1, 15, 14, 0);
        Screening screening = new Screening(1, movie, nonMovieDay, nonMovieDay.plusMinutes(120), auditorium);
        Money originalPrice = new Money(10000);
        Ticket ticket = new Ticket(screening, seat, originalPrice);

        // when
        Ticket resultTicket = movieDayTicketDiscount.discountTicket(ticket);

        // then
        assertThat(resultTicket.price().amount()).isEqualTo(10000);
        assertThat(resultTicket).isEqualTo(ticket);
    }
}
