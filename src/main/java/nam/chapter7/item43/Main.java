package nam.chapter7.item43;


import com.google.common.base.Function;
import com.google.common.base.Supplier;

import java.io.IOException;
import java.time.Instant;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) {
        // 정적 메서드 참조
        Function<String, Integer> parseIntMethod = Integer::parseInt;
        Function<String, Integer> parseIntLambda = str -> Integer.parseInt(str);

        // 한정적 (인스턴스) 메서드 참조
        Function<Instant, Boolean> isAfterMethod = Instant.now()::isAfter;
        Instant then = Instant.now();
        Function<Instant, Boolean> isAfterLambda = t -> then.isAfter(t);

        // 비한정적 (인스턴스) 메서드 참조
        Function<String, String> lowerCaseMethod = String::toLowerCase;
        Function<String, String> lowerCaseLambda = str -> str.toLowerCase();

        // 클래스 생성자 메서드 참조
        Supplier<TreeMap<Integer, String>> treeMapConstructorMethod = TreeMap<Integer, String>::new;
        Supplier<TreeMap<Integer, String>> treeMapConstructorLambda = () -> new TreeMap<Integer, String>();

        // 배열 생성자 메서드 참조
        Function<Integer, int[]> arrayConstructorMethod = int[]::new;
        Function<Integer, int[]> arrayConstructorLambda = len -> new int[len];
    }

    @FunctionalInterface
    public interface CheckedFunction<T, R> {
        R method(T t) throws IOException;
    }
}
