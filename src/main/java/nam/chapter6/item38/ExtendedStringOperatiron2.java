package nam.chapter6.item38;

import java.util.function.Function;

public class ExtendedStringOperatiron2 {

    public interface StringOperation {
        String getDescription();

        String apply(String input);
    }

    public enum BasicStringOperation implements StringOperation {
        TRIM("Removing leading and trailing spaces.", String::trim),
        TO_UPPER("Changing all characters into upper case.", String::toUpperCase),
        REVERSE("Reversing the given string.", input -> new StringBuilder(input).reverse().toString());

        private String description;
        private Function<String, String> operation;

        BasicStringOperation(String description, Function<String, String> operation) {
            this.description = description;
            this.operation = operation;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String apply(String input) {
            return operation.apply(input);
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
