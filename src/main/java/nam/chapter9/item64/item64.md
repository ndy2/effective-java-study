# Item64 - 객체는 인터페이스를 사용해 참조하라

### 적합한 인터페이스만 있다면 매개변수뿐 아니라 반환값, 변수, 필드를 전부 인터페이스 타입으로 선언하라.

> [!QUOTE] 
> 부모는 마음이 넓기 때문에 자식을 받을수 있다. 
>  ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎  ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎  ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎   - 김영한 -

**나쁜 예**
```java
LinkedHashSet<Son> sons = new LinkedHashSet<>();
```
**좋은 예**
```java
Set<Son> sons = new LinkedHashSet<>();
```

**개인적인 생각.**
- item64 는 가장 낮은 레벨에서 수행되는 관심사의 분리
- Set 이던 LinkedHashSet 이던 `sons` 를 사용하는 코드에서는 관심이 없다.

### 적합한 인터페이스가 없다면 당연히 클래스로 참조해야 한다.

- **Case 1.** 값 클래스
e.g.) String, BigInteger

- **Case 2.** 클래스 기반으로 작성된 프렘워크가 제공하는 객체
e.g.) Optional

이런 경우라도 적절한 base class 가 제공된다면 구체 구현체가 아닌 적절한 수준의 base class 로 참조하자. e.g. OutputStream

- **Case 3.** 클래스가 인터페이스에는 없는 특별한 메서드를 제공하는 경우
e.g.) PriorityQueue
