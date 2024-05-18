# Item69 - 예외는 진짜 예외 상황에만 사용하라

 
> [!QUOTE] 핵심 요약
> 예외는 오직 예외 상황에서만 써야한다. 절대로 일상적인 제어 흐름용으로 쓰여선 안 된다.

정상적인 제어 흐름에서 예외를 사용 하면 아래 측면에서 단점이 있다.

1. JVM 의 최적화를 지원받지 어렵다. 따라서 느리다.
2. 나아가 코드를 헷갈리게 하고 버그를 숨겨 디버깅을 어렵게 한다.

앞서 다른 아이템에서 살펴보았듯이 메서드가 발생 시킬 수 있는 예외 상황을 명확히 알고 문서화 하자.


### Hi ChatGPT

Q. Java 에서 정상 흐름에 예외를 사용해 처리하는 예제를 보여줘

A. 사용자 입력 처리를 예외를 통해 검증하는 예시 

```java
import java.util.Scanner;

public class NumberParser {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a number:");

        while (true) {
            String input = scanner.nextLine();

            try {
                int number = Integer.parseInt(input);
                System.out.println("You entered the number: " + number);
                break; // 정상적으로 숫자를 입력하면 루프를 종료합니다.
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, please try again.");
                // 예외가 발생하면 다시 입력을 받도록 루프를 계속합니다.
            }
        }

        scanner.close();
    }
}
```
위와 유사한 코드를 적용해야 했던 사례 - [java.net.IDN#toASCII](https://github.com/openjdk/jdk/blob/master/src/java.base/share/classes/java/net/IDN.java#L168)
