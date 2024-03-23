package nam.chapter7.item48;

import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        Stream.iterate(1, i -> {
                    int result = i + 1;
                    System.out.println("result : " + result);
                    return result;
                })
                .parallel()
                .limit(5)
                .forEach(System.out::println);
    }
}
