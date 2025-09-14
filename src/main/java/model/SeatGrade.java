package model;

enum SeatGrade {
    S(18_000), A(15_000), B(12_000);

    final int price;

    SeatGrade(int price) {
        this.price = price;
    }
}

