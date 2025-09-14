package model;

import model.rules.DefaultSeatGradeRule;
import org.junit.jupiter.api.Test;

public class TheaterTest {
    @Test
    void 마지막_행이_F이고_마지막_열이_10이면_60개의_좌석을_가진다() {
        Theater theater = Theater.of(
                'F',
                10,
                new DefaultSeatGradeRule());

        assert theater.seats.size() == 60;
    }

    @Test
    void 마지막_행이_C이고_마지막_열이_5이면_15개의_좌석을_가진다() {
        Theater theater = Theater.of(
                'C',
                5,
                new DefaultSeatGradeRule());
        assert theater.seats.size() == 15;
    }
}
