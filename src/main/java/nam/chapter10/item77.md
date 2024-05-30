# Item 77 예외를 무시하지 말라

아래와 같은 코드를 지양하자.

```java
// catch 블록을 비워두면 예외가 무시된다. 아주 의심스러운 코드다!
try {
    ...
} catch (SomeException e) {
}
```

예외를 무시하기로 했다면 `catch` 블록 안에 그렇게 결정한 이유를 주석으로 남기고 예외 변수 이름도 `ignore`로 바꿔놓도록 하자.

```java
try {
    ...
} catch (SomeException ignored) {
}
```

모든 예외는 무시되면 프로그램은 오류를 내재한 채 동작한다.
최소한 로그라도 한 줄 출력하자.
