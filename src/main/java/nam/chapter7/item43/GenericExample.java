package nam.chapter7.item43;

import com.google.common.base.Function;


interface IdentityFunction<T> {
    T identity(T value);
}
public class GenericExample {

    // 제네릭 함수 예시
    public static void main(String[] args) {
        // 제네릭 함수 타입을 사용하는 람다 표현식
        IdentityFunction<String> stringIdentity = value -> value;
        IdentityFunction<Integer> intIdentity = value -> value;


        // 람다 표현식을 통한 제네릭 함수 호출
        String resultString = stringIdentity.identity("Hello, Generic!");
        Integer resultInt = intIdentity.identity(42);

        System.out.println("Result String: " + resultString);
        System.out.println("Result Integer: " + resultInt);
    }
}
