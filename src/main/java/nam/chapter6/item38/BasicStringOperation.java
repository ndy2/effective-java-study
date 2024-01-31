package nam.chapter6.item38;

public enum BasicStringOperation {
    TRIM("Removing leading and trailing spaces."),
    TO_UPPER("Changing all characters into upper case."),
    REVERSE("Reversing the given string.");

    private String description;

    BasicStringOperation(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
