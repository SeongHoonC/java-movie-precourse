package model.common;

public record Money(
        int amount
) {
    public Money {
        if (amount <= 0) {
            throw new IllegalArgumentException(ERROR_NEGATIVE_AMOUNT);
        }
    }

    public Money minus(Money other) {
        return new Money(this.amount - other.amount);
    }

    public Money plus(Money other) {
        return new Money(this.amount + other.amount);
    }

    public Money times(double percent) {
        return new Money((int) (this.amount * percent));
    }

    static final String ERROR_NEGATIVE_AMOUNT = "돈은 양의 정수여야 합니다.";
}
