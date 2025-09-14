package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class SeatNumberTest {
    @ParameterizedTest
    @DisplayName("행은 A~Z 대문자여야 한다.")
    @ValueSource(strings = {"a", "1", "*", "aa", "Az", "한"})
    void test1(String row) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new SeatNumber(row, 1));
        assertEquals(SeatNumber.ERROR_SEAT_ROW, ex.getMessage());

        assertThrows(IllegalArgumentException.class, () -> new SeatNumber(row, 5), "행은 A~Z 대문자여야 한다.");
    }

    @ParameterizedTest
    @DisplayName("열은 1 이상 100 이하이어야 한다.")
    @ValueSource(ints = {0, -1, -100, 101, 200})
    void test2(int column) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new SeatNumber("A", column));
        assertEquals(SeatNumber.ERROR_SEAT_COLUMN, ex.getMessage());

        assertThrows(IllegalArgumentException.class, () -> new SeatNumber("B", column), "열은 1 이상 100 이하이어야 한다.");
    }
}