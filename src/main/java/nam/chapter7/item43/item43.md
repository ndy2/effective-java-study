# Item 43 - 람다보다는 메서드 참조를 사용하라

> [!quote] 핵심 정리
> 메서드 참조는 람다의 간단명료한 대안이 될 수 있다. 메서드 참조 쪽이 짧고 명확하다면 메서드 참조를 쓰고, 그렇지 않을 때만 람다를 사용하라.

## 메서드 참조의 종류


```java
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
```

