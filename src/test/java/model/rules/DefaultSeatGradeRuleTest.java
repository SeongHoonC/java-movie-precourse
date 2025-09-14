package model.rules;

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
                () -> assertEquals(model.SeatGrade.B, rule.getSeatGrade(new model.SeatNumber(row, 1))),
                () -> assertEquals(model.SeatGrade.B, rule.getSeatGrade(new model.SeatNumber(row, 5))),
                () -> assertEquals(model.SeatGrade.B, rule.getSeatGrade(new model.SeatNumber(row, 10))),
                () -> assertEquals(model.SeatGrade.B, rule.getSeatGrade(new model.SeatNumber(row, 100)))
        );
    }

    @ParameterizedTest
    @ValueSource(chars = {'E', 'F', 'G', 'H'})
    void E행부터_H행은_열에_상관없이_S등급_좌석이다(char row) {
        DefaultSeatGradeRule rule = new DefaultSeatGradeRule();
        assertAll(
                () -> assertEquals(model.SeatGrade.S, rule.getSeatGrade(new model.SeatNumber(row, 1))),
                () -> assertEquals(model.SeatGrade.S, rule.getSeatGrade(new model.SeatNumber(row, 5))),
                () -> assertEquals(model.SeatGrade.S, rule.getSeatGrade(new model.SeatNumber(row, 10))),
                () -> assertEquals(model.SeatGrade.S, rule.getSeatGrade(new model.SeatNumber(row, 100)))
        );
    }

    @ParameterizedTest
    @ValueSource(chars = {'I', 'J', 'K', 'L', 'X', 'Y', 'Z'})
    void A행부터_H행이_아니면_열에_상관없이_A등급_좌석이다(char row) {
        DefaultSeatGradeRule rule = new DefaultSeatGradeRule();
        assertAll(
                () -> assertEquals(model.SeatGrade.A, rule.getSeatGrade(new model.SeatNumber(row, 1))),
                () -> assertEquals(model.SeatGrade.A, rule.getSeatGrade(new model.SeatNumber(row, 5))),
                () -> assertEquals(model.SeatGrade.A, rule.getSeatGrade(new model.SeatNumber(row, 10))),
                () -> assertEquals(model.SeatGrade.A, rule.getSeatGrade(new model.SeatNumber(row, 100)))
        );
    }
}