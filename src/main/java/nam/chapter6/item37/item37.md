# Item 37. Enum 의 ordinal 을 index 로 사용하지 마라.

아니 그냥 ordinal 을 쓰지마라.

## Java Enum 의 `ordinal`

Java 의 모든 Enum 클래스는 `java.lang.Enum` 을 자동으로 상속한다. ordinal 메서드는 바로 이 `java.lang.Enum` 에 정의 되어 있다.
이 ordinal 의 기원을 `java.lang.Enum` 에서 찾아 가보자.

### `java.lang.Enum` 의 생성자

```java title="java.lang.Enum 의 protected 생성자"
// @formatter:off
/**
 * 유일한 생성자.  Programmers 는 이 생성자를 호출 할 수 없다.
 * Enum 클래스 선언시 컴파일러에 의해 자동으로 활용된다.
 *
 * @param name - The name of this enum constant, which is the identifier
 *               used to declare it.
 * @param ordinal - The ordinal of this enumeration constant (its position
 *         in the enum declaration, where the initial constant is assigned
 *         an ordinal of zero).
 */
protected Enum(String name,int ordinal){
    this.name=name;
    this.ordinal=ordinal;
}
// @formatter:on
```

컴파일러에 의해 자동으로 활용되는 위 생성자는 `java.lang.Enum` 유2 한 필드 `name` 과 `ordinal` 을 세팅한다.
`java.lang.Enum` 은 두 필드에 각각 `name()`, `ordinal()` 라는 필드명과 동일한 게터를 제공 한다.

p.s. javaDoc 을 살펴보면 `ordinal()` 뿐만이 아니라 그냥 `name()` 도 사용하지 말라고 가이드 한다.
그 이유를 생각해보자.

### `java.lang.Enum.ordinal()` Sample Code

```java title="java enum ordinal sample code"
enum Type {
    A, B, C
}

    @Test
    void ordinal() {
        assertThat(Type.A.ordinal()).isEqualTo(0);
        assertThat(Type.B.ordinal()).isEqualTo(1);
        assertThat(Type.C.ordinal()).isEqualTo(2);
    }
```

### `ordinal` 을 사용하면 안되는 이유

ordinal 은 그 이름 처럼 Enum 이 정의된 순서를 zero-based index 로 반환하는 메서드이다.
enum 이 정의된 순서를 활용해 프로그래밍이 필요하다는 상황이 정말 그 순서가 의미를 가지는 지 의심할 필요가 있다.

즉, Enum 의 순서를 아무렇게나 섞어도 비즈니스 로직에는 영향이 없어야 한다.
대부분의 경우 그 순서 보다는 전체 Enum 자체에 대한 정보가 필요한 경우가 대부분일 것이다.

### `ordinal` 의 대안

#### explicitly define ordinal field - item35

만약 Enum 의 순서가 의미를 가진다면 명시적으로 Enum 에 필드를 정의하여 활용하는 것이 좋은 방법이다.

```java title="index 필드를 명시적으로 가지는 Enum"
enum TypeWithIndex {
    B(1), A(0), C(2);

    int index;

    TypeWithIndex(int index) {
        this.index = index;
    }
}
```

#### EnumMap - item37

```java title="EnumMap Sample Code"
// @formatter:off
@Test
void enumMap() {
    // given
    EnumMap<Type, Object> enumMap = new EnumMap<>(Type.class);

    // when
    for (Type t : Type.values()) {
        enumMap.put(t, t.toString() + t.toString());
    }

    // then
    assertThat(enumMap).containsExactlyInAnyOrderEntriesOf(Map.of(
            Type.A, "AA",
            Type.B, "BB",
            Type.C, "CC"
    ));
}
// @formatter:on
```

EnumMap 의 구현은 배열을 활용하므로 직접 ordinal 을 이용해 Enum 을 활용하는 것과 성능적으로도 별 차이가 없다.
EnumMap 의 생성자가 Enum 클래스의 Class 를 통해 Runtime Generic Type 정보 (see item 33) 를 받는 것이 인상적이다.
생성자는 이 Runtime Type 정보를 이용해 생성 단계에서 내부적으로 활용할 배열의 크기를 미리 결정한다.
