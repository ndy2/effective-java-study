# Item 72 - 표준 예외를 사용하라

숙련된 프로그래머는 그렇지 못한 프로그래머보다 더 많은 코드를 재사용한다.
예외도 마찬가지로 재사용하는 것이 좋으며, 자바 라이브러리는 대부분 API 에서 쓰기에 충분한 수의 예외를 제공한다.

## 표준 예외를 재사용하라

- 표준 예외를 사용하면 다른사람이 API를 익히고 사용하기 쉬워진다 (많은 개발자들이 이미 익숙하게 사용하기 때문)
- 표준 예외를 사용한 API는 다른 개발자가 API를 사용하더라도 낯선 예외를 사용하지 않아 코드의 가독성이 높아진다.
- 예외 클래스 수가 적을수록 메모리 사용량도 줄고 클래스를 로딩하는 시간도 적게 걸린다.  


## 가장 흔한 예외

- java.lang.NullPoinerException
	- 객체 참조가 `null`인 상태에서 해당 객체의 메서드나 속성에 접근하려고 할 때 발생

```java
String str = null;
str.length(); // NullPointerException 발생
```

- java.lang.IllegalArgumentException
	- 메서드에 전달된 인자가 유효하지 않을 때 발생합니다.

```java
Thread t = new Thread();
t.setPriority(11); // IllegalArgumentException 발생 (스레드 우선순위는 1~10 범위)
```

- java.lang.IllegalStateException
	- 메서드가 호출될 때 객체의 상태가 유효하지 않은 경우에 발생합니다. 이는 보통 메서드가 호출될 시점의 객체 상태가 올바르지 않음을 나타냅니다.

```java
List<String> list = new ArrayList<>();
Iterator<String> iterator = list.iterator();
iterator.next(); // IllegalStateException 발생 (요소가 없기 때문에 next()를 호출할 수 없음)
```

### 개인적인 사례

팀에서 사용하는 `thrift` 에서는 인터페이스에 표준 예외를 사용할 수 없고 직접 예외를 정의 해야하고 팀내에서 그런 지식이 잘 공유 되어 있기 때문에 자주 사용하는 표준예외에 매칭되는 커스텀 예외를 활용함

```thrift
exception MyIllegalArgumentException {
    1: required string message
}
```

이런 thrift 인터페이스를 thrift-gen 하면 아래와 같은 java 클래스가 생성되는 방식


```java
public class MyIllegalArgumentException extends TException implements TBase<MyIllegalArgumentException, MyIllegalArgumentException._Fields> {
    private static final TStruct STRUCT_DESC = new TStruct("MyThriftException");
    private static final TField MESSAGE_FIELD_DESC = new TField("message", TType.STRING, (short) 1);

    public String message;

    public MyIllegalArgumentException() {
    }

    public MyIllegalArgumentException(String message) {
        this();
        this.message = message;
    }
    
	// ...
	// 필드의 유효성 체크 직렬화/역직렬화 등의 기능을 제공함
}

```