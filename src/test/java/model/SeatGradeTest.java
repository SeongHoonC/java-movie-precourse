package model;

import org.junit.jupiter.api.Test;

class SeatGradeTest {

    @Test
    void S등급_좌석은_18_000원_이다() {
        assert SeatGrade.S.price == 18_000;
    }

    @Test
    void A등급_좌석은_15_000원_이다() {
        assert SeatGrade.A.price == 15_000;
    }

    @Test
    void B등급_좌석은_12_000원_이다() {
        assert SeatGrade.B.price == 12_000;
    }

}