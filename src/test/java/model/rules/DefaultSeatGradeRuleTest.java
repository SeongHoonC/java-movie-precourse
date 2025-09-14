package model.rules;

import model.seat.SeatGrade;
import model.seat.SeatNumber;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultSeatGradeRuleTest {

    @ParameterizedTest
    @ValueSource(chars = {'A', 'B', 'C', 'D'})
    void A행부터_D행은_열에_상관없이_B등급_좌석이다(char row) {
        DefaultSeatGradeRule rule = new DefaultSeatGradeRule();
        assertAll(
                () -> assertEquals(SeatGrade.B, rule.getSeatGrade(new SeatNumber(row, 1))),
                () -> assertEquals(SeatGrade.B, rule.getSeatGrade(new SeatNumber(row, 5))),
                () -> assertEquals(SeatGrade.B, rule.getSeatGrade(new SeatNumber(row, 10))),
                () -> assertEquals(SeatGrade.B, rule.getSeatGrade(new SeatNumber(row, 100)))
        );
    }

    @ParameterizedTest
    @ValueSource(chars = {'E', 'F', 'G', 'H'})
    void E행부터_H행은_열에_상관없이_S등급_좌석이다(char row) {
        DefaultSeatGradeRule rule = new DefaultSeatGradeRule();
        assertAll(
                () -> assertEquals(SeatGrade.S, rule.getSeatGrade(new SeatNumber(row, 1))),
                () -> assertEquals(SeatGrade.S, rule.getSeatGrade(new SeatNumber(row, 5))),
                () -> assertEquals(SeatGrade.S, rule.getSeatGrade(new SeatNumber(row, 10))),
                () -> assertEquals(SeatGrade.S, rule.getSeatGrade(new SeatNumber(row, 100)))
        );
    }

    @ParameterizedTest
    @ValueSource(chars = {'I', 'J', 'K', 'L', 'X', 'Y', 'Z'})
    void A행부터_H행이_아니면_열에_상관없이_A등급_좌석이다(char row) {
        DefaultSeatGradeRule rule = new DefaultSeatGradeRule();
        assertAll(
                () -> assertEquals(SeatGrade.A, rule.getSeatGrade(new SeatNumber(row, 1))),
                () -> assertEquals(SeatGrade.A, rule.getSeatGrade(new SeatNumber(row, 5))),
                () -> assertEquals(SeatGrade.A, rule.getSeatGrade(new SeatNumber(row, 10))),
                () -> assertEquals(SeatGrade.A, rule.getSeatGrade(new SeatNumber(row, 100)))
        );
    }
}