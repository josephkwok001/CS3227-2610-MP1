package seedu.budgie.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Shared matching rules for {@code find KEYWORD}.
 */
public final class EntryMatcher {

    private EntryMatcher() {
    }

    /**
     * Returns whether category, description, or amount matches {@code keyword}.
     * Text matches are case-insensitive substrings. Amount matches by numeric value or the
     * displayed {@code $12.50} form, not by a digit appearing inside a larger amount.
     *
     * @param amount money value
     * @param category category without slash
     * @param description free-text description
     * @param keyword user search text
     * @return {@code true} if the entry matches
     */
    public static boolean matches(BigDecimal amount, String category, String description, String keyword) {
        String needle = keyword.trim().toLowerCase();
        if (needle.isEmpty()) {
            return false;
        }
        if (category.toLowerCase().contains(needle) || description.toLowerCase().contains(needle)) {
            return true;
        }
        String plain = amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
        String trimmedKeyword = keyword.trim();
        if (plain.equals(needle) || ("$" + plain).equalsIgnoreCase(trimmedKeyword)) {
            return true;
        }
        try {
            BigDecimal wanted = new BigDecimal(trimmedKeyword);
            return amount.compareTo(wanted) == 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
