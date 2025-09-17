package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    @DisplayName("만원 Money 는 만원이다.")
    void createMoneyWithValidAmount() {
        // given
        int amount = 10000;

        // when
        Money money = new Money(amount);

        // then
        assertThat(money).isEqualTo(new Money(10000));
    }


    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100, -1000})
    @DisplayName("0이나 음수 금액으로 Money 객체를 생성하면 예외가 발생한다")
    void createMoneyWithNegativeAmountThrowsException(int negativeAmount) {
        // when & then
        assertThatThrownBy(() -> new Money(negativeAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("돈은 양의 정수여야 합니다.");
    }

    @Test
    @DisplayName("Money 는 더할 수 있다")
    void addMoney() {
        // given
        Money money1 = new Money(5000);
        Money money2 = new Money(3000);

        // when
        Money result = money1.plus(money2);

        // then
        assertThat(result.amount()).isEqualTo(8000);
    }

    @Test
    @DisplayName("Money 는 뺄 수 있다")
    void subtractMoney() {
        // given
        Money money1 = new Money(10000);
        Money money2 = new Money(3000);

        // when
        Money result = money1.minus(money2);

        // then
        assertThat(result.amount()).isEqualTo(7000);
    }

    @Test
    @DisplayName("더 큰 금액을 빼면 음수가 되어 예외가 발생한다")
    void subtractLargerAmountThrowsException() {
        // given
        Money money1 = new Money(3000);
        Money money2 = new Money(5000);

        // when & then
        assertThatThrownBy(() -> money1.minus(money2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("돈은 양의 정수여야 합니다.");
    }
}
