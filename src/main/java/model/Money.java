package model;

public record Money(
        int amount
) {
    public Money(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(ERROR_NEGATIVE_AMOUNT);
        }
        this.amount = amount;
    }

    public Money minus(Money other) {
        return new Money(this.amount - other.amount);
    }

    public Money plus(Money other) {
        return new Money(this.amount + other.amount);
    }

    static final String ERROR_NEGATIVE_AMOUNT = "돈은 양의 정수여야 합니다.";
}
