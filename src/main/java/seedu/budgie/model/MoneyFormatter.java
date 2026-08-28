package seedu.budgie.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Formats money amounts with two decimal places using {@link RoundingMode#HALF_UP}.
 */
public final class MoneyFormatter {

    private MoneyFormatter() {
    }

    /**
     * Returns a plain amount string for save files and numeric comparisons (e.g. {@code 12.50}).
     *
     * @param amount money value
     * @return formatted amount without a currency symbol
     */
    public static String formatPlain(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    /**
     * Returns a display amount with a dollar sign (e.g. {@code $12.50}).
     *
     * @param amount money value
     * @return formatted amount with {@code $} prefix
     */
    public static String formatDisplay(BigDecimal amount) {
        return "$" + formatPlain(amount);
    }

    /**
     * Returns a display amount with a dollar sign. Negative values use {@code -$x.xx}.
     *
     * @param amount money value (may be negative for summary net)
     * @return formatted signed display amount
     */
    public static String formatSignedDisplay(BigDecimal amount) {
        String plain = formatPlain(amount.abs());
        if (amount.signum() < 0) {
            return "-$" + plain;
        }
        return "$" + plain;
    }
}
