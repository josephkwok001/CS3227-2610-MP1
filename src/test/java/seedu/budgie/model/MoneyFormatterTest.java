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
    public void formatDisplay_addsDollarSign() {
        assertEquals("$12.50", MoneyFormatter.formatDisplay(new BigDecimal("12.5")));
        assertEquals("$2500.00", MoneyFormatter.formatDisplay(new BigDecimal("2500")));
    }

    @Test
    public void formatSignedDisplay_handlesNegativeAndZero() {
        assertEquals("-$15.50", MoneyFormatter.formatSignedDisplay(new BigDecimal("-15.50")));
        assertEquals("$2500.00", MoneyFormatter.formatSignedDisplay(new BigDecimal("2500")));
        assertEquals("$0.00", MoneyFormatter.formatSignedDisplay(BigDecimal.ZERO));
    }
}
