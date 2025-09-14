package model;

import model.seat.SeatNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class SeatNumberTest {
    @ParameterizedTest
    @DisplayName("행은 A~Z 대문자여야 한다.")
    @ValueSource(chars = {'a', '1', '*', 'z', '!', ' '})
    void test1(char row) {
        assertThrows(IllegalArgumentException.class, () -> new SeatNumber(row, 5), "행은 A~Z 대문자여야 한다.");
    }

    @ParameterizedTest
    @DisplayName("열은 1 이상 100 이하이어야 한다.")
    @ValueSource(ints = {0, -1, -100, 101, 200})
    void test2(int column) {
        assertThrows(IllegalArgumentException.class, () -> new SeatNumber('B', column), "열은 1 이상 100 이하이어야 한다.");
    }
}