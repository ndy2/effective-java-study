package nam.chapter8.item55;

import java.util.Map;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {

    }

    static class MyMap {

        Map<String, String> underlying = Map.of("key1", "val1", "key2", "val2");

        // return value is nullable
        String get1(String key) {
            return underlying.get(key);
        }

        // return value is not nullable
        String get2(String key) {
            if (!underlying.containsKey(key)) {
                throw new IllegalArgumentException("no such key : " + key);
            }
            return underlying.get(key);
        }

        // return value is Optional
        Optional<String> get3(String key) {
            // equivalent :
            // return Optional.ofNullable(underlying.get(kye))
            if (!underlying.containsKey(key)) {
                return Optional.empty();
            }
            return Optional.of(underlying.get(key));
        }
    }
}
