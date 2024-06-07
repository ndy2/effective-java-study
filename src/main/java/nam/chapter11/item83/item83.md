# Item83

지연 초기화 (lazy initialization) 은 필드 초기화를 실제로 그 값이 쓰일 때까지 미루는 것이다. 값을 사용하는 곳이 없다면 필드는 결코 초기화되지 않는다.

## 인스턴스 필드를 초기화 하는 방법

### 1. 일반적인 방법

인스턴스 필드를 초기화하는 일반적인 방법

`private final FieldType field = computeFieldValue();`

### 2. 지연 초기화

#### 2.0 일반적인 지연 초기화

동시성을 고려하지 않은 일반적인 지연 초기화 기법이다.

```java
public class LazyInitializationExample {
    private SomeObject someObject;

    public SomeObject getSomeObject() {
        if (someObject == null) {
            someObject = new SomeObject();
        }
        return someObject;
    }
}

```

#### 2.1. Syncrhonized getter

```java
private FieldType field;

private synchronized FieldType getField() {
    if (field == null)
        field = computeFieldValue();
    return field;
}
```

#### Lazy initialization Holder Class

**정적 필드**용 Lazy initialization Holder Class (`FieldHolder`)

```java
private static class FieldHolder {
    static final FieldType field = computeFieldValue();
}

private static FieldType getField() { return FieldHolder.field;}
```

getField가 처음 호출되는 순간 FieldHolder.field가 처음 읽히면서, 비로소 FieldHolder 클래스 초기화를 촉발한다. 이 관용구의 멋진 점은 getField 메서드가 필드에 접근하면서 동기화를 전혀 사용하지 않으니 성능이 느려질 거리가 전혀 없다는 것이다.

일반적인 VM은 오직 클래스를 초기화할 때만 필드 접근을 동기화할 것이다. 클래스 초기화가 끝난 후에는 VM이 동기화 코드를 제거하여, 그 다음부터는 아무런 검사나 동기화 없이 필드에 접근하게 된다.

**인스턴스 필드**용 Double-Checked Locking

```java
private volatile FieldType field; // 필드가 초기화된 후로는 동기화 하지 않으므로 반드시 volatile 로 선언!

private FieldType getField() {
    FieldType result = field; // 필드를 선언한 이유?
                              // 이미 초기화 된 필드는 딱 한번만 읽도록 함
                              // 저수준 병렬 프래그래밍에서 최적화가 적용된다.
    if (result != null) { // 첫 번째 검사 (락 사용 안 함)
        return result;
    
    synchronized(this) {
        if (field == null) // 두 번째 검사 (락 사용)
            field = computeFieldValue();
        return field;
    }
}
```