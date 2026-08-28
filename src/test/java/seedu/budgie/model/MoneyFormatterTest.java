package seedu.budgie.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class MoneyFormatterTest {

    @Test
    public void formatPlain_twoDecimalPlaces() {
        assertEquals("12.50", MoneyFormatter.formatPlain(new BigDecimal("12.5")));
        assertEquals("2500.00", MoneyFormatter.formatPlain(new BigDecimal("2500")));
    }

    @Test
    public void formatPlain_oneDecimalInput_showsTwoDecimals() {
        assertEquals("12.50", MoneyFormatter.formatPlain(new BigDecimal("12.5")));
        assertEquals("3.00", MoneyFormatter.formatPlain(new BigDecimal("3")));
    }

    @Test
    public void formatPlain_twoDecimalInput_keepsTwoDecimals() {
        assertEquals("12.50", MoneyFormatter.formatPlain(new BigDecimal("12.50")));
        assertEquals("2500.00", MoneyFormatter.formatPlain(new BigDecimal("2500.00")));
    }

    @Test
    public void formatPlain_halfUp_roundsToTwoDecimals() {
        assertEquals("2.56", MoneyFormatter.formatPlain(new BigDecimal("2.555")));
    }

    @Test
    public void formatDisplay_addsDollarSign() {
        assertEquals("$12.50", MoneyFormatter.formatDisplay(new BigDecimal("12.5")));
        assertEquals("$2500.00", MoneyFormatter.formatDisplay(new BigDecimal("2500")));
    }

    @Test
    public void formatDisplay_matchesDollarPlusPlain() {
        BigDecimal[] samples = {
            new BigDecimal("12.5"),
            new BigDecimal("12.50"),
            new BigDecimal("2500"),
            new BigDecimal("0.01")
        };
        for (BigDecimal amount : samples) {
            assertEquals("$" + MoneyFormatter.formatPlain(amount), MoneyFormatter.formatDisplay(amount));
        }
    }

    @Test
    public void formatSignedDisplay_handlesNegativeAndZero() {
        assertEquals("-$15.50", MoneyFormatter.formatSignedDisplay(new BigDecimal("-15.50")));
        assertEquals("$2500.00", MoneyFormatter.formatSignedDisplay(new BigDecimal("2500")));
        assertEquals("$0.00", MoneyFormatter.formatSignedDisplay(BigDecimal.ZERO));
    }

    @Test
    public void formatSignedDisplay_smallNegative_usesMinusDollarPrefix() {
        assertEquals("-$3.00", MoneyFormatter.formatSignedDisplay(new BigDecimal("-3")));
        assertEquals("-$0.01", MoneyFormatter.formatSignedDisplay(new BigDecimal("-0.01")));
    }

    @Test
    public void formatSignedDisplay_positive_hasNoMinusSign() {
        assertEquals("$12.50", MoneyFormatter.formatSignedDisplay(new BigDecimal("12.50")));
        assertEquals("$0.00", MoneyFormatter.formatSignedDisplay(BigDecimal.ZERO));
    }
}
