# Item79: Avoid excessive synchronization

이 아이템을 설명하기 아주 좋은 예시는 면접 단골 질문인 Hashtable, HashMap, 그리고 ConcurrentHashMap 의 비교이다.

![[hashtable.png]]


**HashMap**

- not thread-safe

**Hashtable**

- Thread-safe
- 메소드에 통으로 synchrnized 걸어버림

**SynchronizedMap**

- Thread-safe
- 내부에 하나 가지고 있는 dummy monitor 객체에 lock을 걸어버리는 방식으로 synchronized 걸어버림

- **전체 맵 동기화**: 모든 메서드가 동기화되어 있어, 모든 읽기와 쓰기 연산이 단일 락을 사용합니다. 이는 여러 스레드가 동시에 접근할 경우 성능 저하를 초래할 수 있습니다.
- **성능**: 높은 경쟁 상황에서 성능이 좋지 않습니다. 모든 접근이 단일 락에 의존하기 때문에, 다수의 스레드가 동시에 접근하면 병목 현상이 발생할 수 있습니다.

**ConcurrentHashMap**

- Thread-safe
- 내부 테이블 (`Node<K,V>[ ] table`)에서 `put`, ~~`get`~~ 의 타겟이 되는 `Node` 에만 synchronized 걸어버림
- 읽기 작업(`get` 메서드 등)은 락을 사용하지 않고 수행됩니다. 이는 읽기 작업의 성능을 극대화합니다.
- 내부 구조가 변경될 가능성이 있는 경우에도 `volatile`을 통해 최신 상태를 읽을 수 있게 합니다.

- **부분 잠금**: `ConcurrentHashMap`은 내부적으로 여러 개의 버킷(bucket)을 사용하고, 각 버킷마다 별도의 락을 적용합니다. 따라서 여러 스레드가 동시에 다른 버킷에 접근할 수 있어 병목 현상이 줄어듭니다.
- **높은 성능**: 동시성 제어를 위한 부분 잠금을 사용하기 때문에, `SynchronizedMap`보다 훨씬 더 나은 성능을 제공합니다. 특히 읽기 작업이 많을 경우 매우 효율적입니다.
- **반복 시 동기화 불필요**: 반복할 때 수동으로 동기화를 적용할 필요가 없습니다. 내부적으로 일관된 상태를 유지하면서도 동시 접근을 허용합니다.


> [!QUESTION] ConcurrentHashMap 이 bucket 에 락을 건다면 버킷이 존재하지 않는 시점에 동시에 여러 쓰레드에서 같은 버킷에 접근한다면?
> CAS - Compare And Swap

```java
  transient volatile Node<K,V>[] table;
  
  /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code key.equals(k)},
     * then this method returns {@code v}; otherwise it returns
     * {@code null}.  (There can be at most one such mapping.)
     *
     * @throws NullPointerException if the specified key is null
     */
    public V get(Object key) {
        Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
        int h = spread(key.hashCode());
        if ((tab = table) != null && (n = tab.length) > 0 &&
            (e = tabAt(tab, (n - 1) & h)) != null) {
            if ((eh = e.hash) == h) {
                if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                    return e.val;
            }
            else if (eh < 0)
                return (p = e.find(h, key)) != null ? p.val : null;
            while ((e = e.next) != null) {
                if (e.hash == h &&
                    ((ek = e.key) == key || (ek != null && key.equals(ek))))
                    return e.val;
            }
        }
        return null;
    }
```

![[concurrentHashMapCode.png]]