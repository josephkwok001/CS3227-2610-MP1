package seedu.budgie.model;

/**
 * A recorded expense or income that can appear in {@code list} output.
 */
public interface Entry {

    /**
     * Returns a numbered-list line, including whether this is an expense or income.
     *
     * @return formatted list line
     */
    String toListLine();

    /**
     * Returns one line for the save file. Descriptions may contain {@code |}.
     *
     * @return encoded entry
     */
    String toFileString();
}
