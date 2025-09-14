package model.seat;

public record SeatNumber(
        char row,
        int column
) {
    public SeatNumber {
        if ( row < 'A' || row > 'Z') {
            throw new IllegalArgumentException(ERROR_SEAT_ROW);
        }
        if (column <= 0 || column > 100) {
            throw new IllegalArgumentException(ERROR_SEAT_COLUMN);
        }

    }

    public static final String ERROR_SEAT_ROW = "행은 A부터 Z까지의 한 문자여야 합니다.";
    public static final String ERROR_SEAT_COLUMN = "열은 1 이상 100 이하의 숫자여야 합니다.";
}
