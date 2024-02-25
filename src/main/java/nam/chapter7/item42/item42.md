# item42 익명 클래스보다는 람다를 사용하라

> [!quote] 참고 자료
> - oracle > java docs>classes and object > nested classes > [Anonymous Classes](https://docs.oracle.com/javase/tutorial/java/javaOO/anonymousclasses.html) 
> - oracle > java docs>classes and object > nested classes > [Lambda Expression](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html) 
> - baeldung > [synthetic constructs in java](https://www.baeldung.com/java-synthetic)

## Terminologies

![[class-types.png]]

**중첩 클래스(nested class)** - 다른 클래스안에 정의된 클래스
- **내부 클래스 (inner class, a.k.a. non-static nested class)** 
- **정적 (중첩) 클래스 (static nested class)** 

**익명 클래스 (anonymous class)** 는 이름을 가지지 않는 내부 클래스(inner class) 이다.
**람다 표현식 (labmda expression)** 은 하나의 추상 메서드 (SAM)를 가지는 인터페이스를 간결하게 instance 화 하는 코드 블럭이다.

## Examples

익명 클래스 예시 - 클래스를 확장 할 수 있다.
```java
new Book("Design Patterns") {
    @Override
    public String description() {
        return "Famous GoF book.";
    }
}
```

람다 표현식 예시
```java
Thread thread = new Thread(() -> {System.out.println("Runnable!");})
```

## 공통점

### 목적

- 한번만 활용 될 클래스 인스턴스의 선언을 쉽게 하자!
- 모두 함수형 프로그래밍 스타일을 지원 하는데 사용된다.

### 변수 접근 제한

두 방식 모두 접근하는 변수가 final/ effective final 이어야 한다.
- Variable used in lambda expression should be final or effectively final
- Variable 'y' is accessed from within inner class, needs to be final or effectively final

![[effectivly-final.png]]

### 직렬화 자제

두 방식 모두 직렬화를 극히 삼가야한다.
> [!quote]
> Serialization of inner classes, including local and anonymous classes, is strongly discouraged.
> 
> 이유 - 내부 클래스에 대해 Java Compiler 는 synthetic construct 라는 것을 자동으로 만드는데 이 구현이 compiler 마다, 즉, Java Version 마다 다를 수 있다.

## 차이점

### 확장 (생성) 가능 대상의 차이

- 익명 클래스
	- Class
	- Interface
- Lambda Expression
	- Functional Interface
### `this` keyword 의 차이

- 익명 클래스 -> this 는 자기 자신
- Lambda -> this 는 바깥 인스턴스

### 람다의 한계

람다식에서는 checked exception 을 처리할때 항상 try-catch 처리를 해 주어야 한다. 

### 심미성

- 익명 클래스 - 아름답지 않다.
- Lambda Expression - 아름답다. 단! 잘 사용 한다면.
