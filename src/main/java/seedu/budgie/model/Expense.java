package seedu.budgie.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * An expense recorded during the current session.
 */
public class Expense {

    private final BigDecimal amount;
    private final String category;
    private final String description;

    /**
     * Creates an expense. {@code amount} must already be positive and at most two decimal places.
     *
     * @param amount money spent
     * @param category category without the leading slash
     * @param description free-text description
     */
    public Expense(BigDecimal amount, String category, String description) {
        assert amount != null : "amount should not be null";
        assert amount.compareTo(BigDecimal.ZERO) > 0 : "amount should be positive";
        assert category != null && !category.isBlank() : "category should not be blank";
        assert description != null && !description.isBlank() : "description should not be blank";
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns a single-line form matching the user-facing command style.
     *
     * @return formatted expense, e.g. {@code $12.50 /food lunch}
     */
    public String toDisplayString() {
        String formattedAmount = amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
        return "$" + formattedAmount + " /" + category + " " + description;
    }
}
