package nam.chapter6.item38;

public class ExtendedStringOperatiron {

    public interface StringOperation {
        String getDescription();

        String apply(String input);
    }

    public enum BasicStringOperation implements StringOperation {
        TRIM("Removing leading and trailing spaces.") {
            @Override
            public String apply(String input) {
                return input.trim();
            }
        },
        TO_UPPER("Changing all characters into upper case.") {
            @Override
            public String apply(String input) {
                return input.toUpperCase();
            }
        },
        REVERSE("Reversing the given string.") {
            @Override
            public String apply(String input) {
                return new StringBuilder(input).reverse().toString();
            }
        };


        private String description;

        BasicStringOperation(String description) {
            this.description = description;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    public enum ExtendedStringOperation implements StringOperation {
        MD5_ENCODE("Encoding the given string using the MD5 algorithm.") {
            @Override
            public String apply(String input) {
                return null;
            }
        },
        BASE64_ENCODE("Encoding the given string using the BASE64 algorithm.") {
            @Override
            public String apply(String input) {
                return null;
            }
        };


        private String description;

        ExtendedStringOperation(String description) {
            this.description = description;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }
}
