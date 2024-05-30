# Item 74 - 메서드가 던지는 모든 예외를 문서화하라


### Unchecked Exception 은 메서드 선언의 throws 에 넣지 말자.

메서드가 던질 수 있는 예외를 @throws로 문서화하되, 비검사 예외는 빼야한다. 검사냐 비검사냐에 따라 API 사용자가 해야 할 일이 달라지므로 이 **둘을 확실히 구분해주는게 좋다.**

```java
/**
 *
 * @return secondField
 * @throws NullPointerException if parameter is null or empty 비검사예외
 * @throws IllegalAccessError if someone access 검사예외
 */
public int getSecondField() throws IllegalAccessError {
    return secondField;
}
```

### Unchecked Exception 의 문서화가 불가능한 경우

외부 라이브러리가 업데이트로 새로운 Unchecked Exception 을 던지는 경우 이를 추적하는것은 매우 어렵다.

###  예외 문서화를 클래스 단위로 올리자

한 클래스에 정의된 많은 메서드가 같은 이유로 같은 예외를 던진다면 그 예외를 (각각의 메서드가 아닌) 클래스 설명에 추가하는 방법도 있다. (취향 차이로 보인다.)

```java
/**
 * @throws IllegalAccessError
 */
public class Test {

	public void add() throws IllegalAccessError  {
		...
	}

	public void minus() throws IllegalAccessError {
		...
	}
```