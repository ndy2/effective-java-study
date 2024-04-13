# item55 - 옵셔널 반환은 신중히 하라


> [!QUOTE] 핵심정리
> 옵셔널 반환은 신중히 하라

메서드가 특정 조건에서 값을 반환 할 수 없을 때 취할 수 있는 선택지 세가지.

```java
Map<String, String> underlying = Map.of("key1", "val1", "key2", "val2");  

// 1번
// return value is nullable  
String get1(String key) {  
    return underlying.get(key);  
}  

// 2번
// return value is not nullable  
String get2(String key) {  
    if (!underlying.containsKey(key)) {  
        throw new IllegalArgumentException("no such key : " + key);  
    }  
    return underlying.get(key);  
}  

// 3번 - introduced in Java8
// return value is Optional  
Optional<String> get3(String key) {  
    // equivalent :  
    // return Optional.ofNullable(underlying.get(kye))    if (!underlying.containsKey(key)) {  
        return Optional.empty();  
    }  
    return Optional.of(underlying.get(key));  
}
```
개인적인 생각 : 웬만한 상황에서 Optional 을 반환 하지 말자.

- 메서드를 사용하는 측에서는 메서드를 만드는 측 (Optional을 반환하는 측) 보다 메서드를 자세히 알 수 없다. None 이 발생 할 수 있는 상황에 대한 정확한 이해가 이루어져야 하는데 그 합의를 가지기가 생각보다 쉽지 않다고 생각함.
	- Optional 반환을 고민을 한다면 그 상황이 이미 충분히 합의되지 않은 상황이라고 생각함.
	- `값이 없다`는 상황이 발생할 수 있는 이유는 메서드에 따라 아주 다양함

- 따라서 Optional 반환을 도입하였다고 전통적인 try-catch 기반의 코드로부터 자유로워지지 않는다.
	- Optional 지지론자 (?) 의 가장 큰 근거가 chaining 을 통한 깔끔한 코드라고 생각함

- 단순히 예외를 던지면 상황에 대한 설명도 쉽고 실행 흐름 (? callstack?) 정상화에 대한 책임을 client 가 주체적으로 할 수 있도록 하게 되어 더 좋다고 생각함.
	- 물론 상황에 맞는 적절한 예외 타입을 추가하거나 예외 메시지를 주어야 한다.
	- 내부 (method 안)에서 Optional 로 바꾸어 주는 것에 대한 느낌은 어짜피 정상화를 위해 대체 값 혹은 Optional 으로 대체하는 과정이 있어야 하는데 그 부분에 대한 책임을 메서드 안 (보통 애플리케이션 레이어가 아님) 으로 모는것 같은 느낌이 들게됨.

p.s. https://stackoverflow.com/questions/42110980/convert-try-to-option-without-losing-error-information-in-scala

### 잡설 1. Scala 의 Option, nulll 처리

- Java 8 의 Optional 과 아주 유사하지만 한가지가 다르다.
- Optional 이 final class 인 java 와 달리 Option 이 `sealed abstract class` 임

![[scala-option.png]]
- Some 과 None 은 각각 존재한다, 존재하지 않는다를 나타내는 Option 의 하위 타입.
- scala 의 pattern-matching (java 의 swith-expression, kotlin 의 when 절과 유사) 과 조합하여 아래의 형태로 아주 잘 활용된다.
```scala
val maybeString: Option[String] = ...

switch(maybeString) {
	case Some(_) => println("string exists!")
	case None    => println("string does not exist!")
}
```
### 잡설 2. JDBC 와 null 처리

Java 의 오래된 Interface 인 ResultSet 을 이용해 Database 의 column 을 직접 처리하는 경우 null 처리가 필요한 경우가 있습니다.column 이 null 인 경우 몇몇 자료형에서는 null 이 아닌 기본값을 채워줍니다. 아래는 대표적인 예시 입니다.

- `rs.getBoolean` -> `false` if column was null
- `rs.getByte, rs.getShort, rs.getInt, rs.getLong, rs.getFloat, rs.getDouble` -> `0`if column was null

예를 들어 null  이 될 수 있는 atime(적용시간) 을 `val atime = Option(rs.getLong("atime"))` 과 같은 스칼라 코드로 작성하면 column 이 null 이라도 atime 은 `None` 이 아닌 `Some(0) 가 저장됩니다.` 이를 해결하기 위해서는 `rs.wasNull` 을 통해 가장 최근에 조회한 column 이 null 이었는지 확인하고 이를 처리해 주어야 합니다.

```scala
// 잘못된 nullable field 조회
val wrongAtime = Option(rs.getLong("atime")) // Some(0) if atime field was null

// 올바른 nullable field 조회
val correctAtime = {
			val selectAtime = rs.getLong("atime")                 if (rs.wasNull()) None
				else Option(selectAtime)
		    } // None if a time field was null
```