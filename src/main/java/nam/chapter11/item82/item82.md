# Item 82

병렬적으로 사용해도 안전한 클래스가 되려면, 어떤 수준의 스레드안정성을 제공하는 클래스인지 문서에 명확하게 남겨야 한다.

- 변경 불가능 (immutable/ `@Immutable`)
	- e.g.) String, Long, BigInteger
- 무조건적 스레드 안정성 (unconditionally thread-safe/ `@ThreadSafe`)
	- 변경이 가능하지만 적절한 내부 동기화 메커니즘을 갖추고 있어 외부적 동기화 메커니즘이 필요 없다.
	- **`private 락 객체 패턴`** 을 통해 구현 할 수 있다.
	- e.g.) Random, ConcurrentHashMap
- 조건부 스레드 안정성 (conditionally thread-safe/ `@ThreadSafe`)
	- 무조건적 스레드 안정성과 유사하지만, 몇몇 스레드는 외부 동기화가 필요하다.
	- e.g.) Collections.synchronized 계열 메서드가 반환하는 wrapper 객체 의 iterator 는 외부적 동기화가 필요하다.
- 스레드 안정성 없음 (`@NotThreadSafe`)
	- ArrayList, HashMap
- 다중 스레드에 적대적 (thread-hostile)
	- 구현 자체가 잘못되어 외부적 동기화 수단으로 처리하더라도 다중 스레드 처리시 안전하지 않은 경우 보통, 동기화 없이 정적 데이터를 변경하기 때문에 발생


## Java 동기화 어노테이션

### 1. `@Immutable`
이 어노테이션은 클래스가 불변(immutable)임을 나타냅니다. 불변 클래스는 상태가 변경되지 않으므로 본질적으로 스레드 안전합니다.

```java
@Immutable
public class ImmutableClass {
    private final int value;

    public ImmutableClass(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
```

### 2. `@ThreadSafe`

이 어노테이션은 클래스나 메서드가 스레드 안전함을 나타냅니다. 즉, 여러 스레드가 동시에 접근하더라도 문제가 발생하지 않음을 보장합니다.

```java
@ThreadSafe
public class ThreadSafeClass {
    // 스레드 안전한 코드
}
```

### 3. `@NotThreadSafe`

이 어노테이션은 클래스나 메서드가 스레드 안전하지 않음을 나타냅니다. 즉, 여러 스레드가 동시에 접근하면 문제가 발생할 수 있음을 의미합니다.

```java
@NotThreadSafe
public class NotThreadSafeClass {
    // 스레드 안전하지 않은 코드
}
```

### 4. `@GuardedBy("lock")`

이 어노테이션은 특정 필드나 메서드가 특정 락(lock)에 의해 보호됨을 나타냅니다. 락은 필드, 메서드, 또는 클래스 수준에서 지정될 수 있습니다. 주로 동기화된 블록이나 메서드에서 사용됩니다.

```java
public class GuardedByExample {
    // private 락 객체 패턴!
    private final Object lock = new Object();

    @GuardedBy("lock")
    private int sharedResource;

    public void safeMethod() {
        synchronized (lock) {
            // sharedResource에 대한 안전한 접근
        }
    }
}
```

## `Synchronized` 메서드와 DoS (Denial of Service) 공격

### `syncrhoized` 메서드의 동작 방식

Java 에서 `syncrhozied` 키워드를 메서드에 사용하면, 해당 메서드는 객체 수준의 락을 획득해야 실행됨. 예를 들어, 아래 코드에서

```java
public class SynchronizedExample {
    public synchronized void criticalSection() {
        // 공유 자원에 접근하는 코드
    }
}

```

- 이 메서드를 호출하는 스레드는 `SynchronizedExample` 객체의 락을 획득해야만 메서드를 실행할 수 있습니다.
- 한 스레드가 이 메서드를 실행 중이라면, 다른 스레드는 해당 객체의 락을 획득할 때까지 대기해야 합니다.``

### DoS 공격의 가능성

DoS 공격은 시스템 리소스를 과도하게 사용하거나 접근을 차단하여, 정상적인 서비스 제공을 방해하는 공격입니다. `synchronized` 메서드의 경우, 클라이언트가 의도적으로 또는 비의도적으로 객체의 락을 장시간 유지하거나, 불필요하게 자주 락을 요청함으로써 다른 클라이언트의 접근을 차단할 수 있습니다. 이는 다음과 같은 방식으로 발생할 수 있습니다:

1. **의도적인 장시간 락 유지**:
    - 악의적인 클라이언트가 `synchronized` 메서드를 호출한 후, 메서드 내에서 장시간 실행되는 작업을 수행합니다.
    - 이로 인해 다른 클라이언트는 해당 객체의 락을 획득하지 못하고 대기 상태에 빠지게 됩니다.
2. **빈번한 락 요청**:
    - 악의적인 클라이언트가 매우 빈번하게 `synchronized` 메서드를 호출하여, 락을 획득하려는 시도로 시스템 리소스를 소모시킵니다.
    - 이로 인해 시스템은 과부하 상태에 빠지고, 정상적인 클라이언트의 요청이 지연됩니다.


```java
public class DoSExample {
    public synchronized void criticalSection() {
        try {
            // 장시간 실행되는 작업
            Thread.sleep(10000); // 10초 동안 슬립
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

### 방지 방법

1. 동기화 범위 축서

```java
public class DoSPreventionExample {
    private final Object lock = new Object();

    public void criticalSection() {
        synchronized (lock) {
            // 짧은 시간 동안만 공유 자원에 접근
        }
        // 다른 작업 수행 (동기화 필요 없음)
    }
}
```


2. 타임아웃 사용

```java
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

public class TimeoutLockExample {
    private final ReentrantLock lock = new ReentrantLock();

    public void criticalSection() {
        try {
            if (lock.tryLock(1000, TimeUnit.MILLISECONDS)) { // 1초 동안 락 시도
                try {
                    // 공유 자원에 접근
                } finally {
                    lock.unlock();
                }
            } else {
                // 타임아웃 후 처리
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```


3. 자원 제한 및 모니터링

문서화 방법

- 산문조로 길게 풀어 쓴다.
- 스레드 안전성 어노테이션을 이용한다.