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
}
```

### 정적 메서드 참조

- 가장 흔하게 사용 되는 예시
- static 메서드를 가르키는 메서드 참조

```java
// 정적 메서드 참조 
Function<String, Integer> parseIntMethod = Integer::parseInt;  
Function<String, Integer> parseIntLambda = str -> Integer.parseInt(str);  

// Integer.parseInt
public static int parseInt(String s) throws NumberFormatException {  
    return parseInt(s,10);  
}
```

### 한정적 (인스턴스) 메서드 참조

- 수신 객체를 특정한다.
- 수신 객체 = 참조 대상 인스턴스 
- 함수 객체가 받는 인수와 참조되는 메서드가 받는 인수가 같다.

```java
// 한정적 (인스턴스) 메서드 참조  
Function<Instant, Boolean> isAfterMethod = Instant.now()::isAfter;  
Instant then = Instant.now();  
Function<Instant, Boolean> isAfterLambda = t -> then.isAfter(t);  

// Instant 의 isAfter
public boolean isAfter(Instant otherInstant) {  
    return compareTo(otherInstant) > 0;  
}
```

### 비 한정적 (인스턴스) 메서드 참조

- 수신 객체를 특정하지 않는다.
- 수신 객체 전달용 parameter 가 parameter 목록의 첫번째로 추가 된다.
- 주로 스트림 파이프라인에서 활용

```java
// 비한정적 (인스턴스) 메서드 참조  
Function<String, String> lowerCaseMethod = String::toLowerCase;  
Function<String, String> lowerCaseLambda = str -> str.toLowerCase();  
  
// String 의 toLowerCase
public String toLowerCase() {  
    return toLowerCase(Locale.getDefault());  
}
```
## 메서드 참조 vs Lambda

- 익명 클래스 vs Lambda 때 보다 심미성에 대한 Case-By-Case 가 더 심하다고 생각됨.
- 파라미터의 이름으로 의미를 드러 낼 수 있는 경우 Lambda 가 더 가독성이 좋을 수 있음.
- 반면 메서드 참조는 메서드 이름 자체로 전체 연산에 대한 의미를 표현 할 수 있음.
- 취향 및 컨벤션에 따라 적절하게 사용하자.
