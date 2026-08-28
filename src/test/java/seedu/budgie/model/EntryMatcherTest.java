package seedu.budgie.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class EntryMatcherTest {

    private static final BigDecimal AMOUNT = new BigDecimal("12.50");
    private static final String CATEGORY = "food";
    private static final String DESCRIPTION = "lunch";

    @Test
    public void matches_categoryCaseInsensitiveSubstring_returnsTrue() {
        assertTrue(EntryMatcher.matches(AMOUNT, CATEGORY, DESCRIPTION, "FOOD"));
        assertTrue(EntryMatcher.matches(AMOUNT, CATEGORY, DESCRIPTION, "ood"));
    }

    @Test
    public void matches_descriptionCaseInsensitiveSubstring_returnsTrue() {
        assertTrue(EntryMatcher.matches(AMOUNT, CATEGORY, DESCRIPTION, "LUNCH"));
        assertTrue(EntryMatcher.matches(AMOUNT, CATEGORY, DESCRIPTION, "nch"));
    }

    @Test
    public void matches_plainAmount_returnsTrue() {
        assertTrue(EntryMatcher.matches(AMOUNT, CATEGORY, DESCRIPTION, "12.50"));
    }

    @Test
    public void matches_dollarAmount_returnsTrue() {
        assertTrue(EntryMatcher.matches(AMOUNT, CATEGORY, DESCRIPTION, "$12.50"));
    }

    @Test
    public void matches_numericAmountWithoutTrailingZero_returnsTrue() {
        assertTrue(EntryMatcher.matches(AMOUNT, CATEGORY, DESCRIPTION, "12.5"));
    }

    @Test
    public void matches_digitSubstringInAmount_returnsFalse() {
        assertFalse(EntryMatcher.matches(AMOUNT, CATEGORY, DESCRIPTION, "1"));
    }

    @Test
    public void matches_emptyKeyword_returnsFalse() {
        assertFalse(EntryMatcher.matches(AMOUNT, CATEGORY, DESCRIPTION, ""));
    }

    @Test
    public void matches_blankKeywordAfterTrim_returnsFalse() {
        assertFalse(EntryMatcher.matches(AMOUNT, CATEGORY, DESCRIPTION, "   "));
    }

    @Test
    public void matches_noOverlap_returnsFalse() {
        assertFalse(EntryMatcher.matches(AMOUNT, CATEGORY, DESCRIPTION, "rent"));
        assertFalse(EntryMatcher.matches(AMOUNT, CATEGORY, DESCRIPTION, "notanumber"));
    }
}
