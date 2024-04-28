package nam.chapter8.item55;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Main {

    public static void main(String[] args) {

    }

    static class MyMap extends Map<String, String> {

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

        /**
         * {@inheritDoc}
         */
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean containsKey(Object key) {
            return false;
        }

        @Override
        public boolean containsValue(Object value) {
            return false;
        }

        @Override
        public String get(Object key) {
            return "";
        }

        @Nullable
        @Override
        public String put(String key, String value) {
            return "";
        }

        @Override
        public String remove(Object key) {
            return "";
        }

        @Override
        public void putAll(@NotNull Map<? extends String, ? extends String> m) {

        }

        @Override
        public void clear() {

        }

        @NotNull
        @Override
        public Set<String> keySet() {
            return Set.of();
        }

        @NotNull
        @Override
        public Collection<String> values() {
            return List.of();
        }

        @NotNull
        @Override
        public Set<Entry<String, String>> entrySet() {
            return Set.of();
        }

        @Override
        public boolean equals(Object o) {
            return false;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }
}
