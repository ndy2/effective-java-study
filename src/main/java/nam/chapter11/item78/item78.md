# Item 78: Synchronize access to shared mutable data

동기화는 다음 두 가지 중요한 역할을 합니다:

1. 여러 스레드가 객체를 일관된 상태로 유지하면서 접근할 수 있도록 한다.
2. 동기화된 메서드나 블록에 진입하는 스레드가 이전에 해당 객체에 대해 수행된 모든 수정 사항을 확실히 볼 수 있게 한다.


> [!NOTE] JLS 17.7 - Non-atomic Treatment of double and long
> 이 섹션에서는 double과 long 타입 변수의 비원자적(non-atomic) 처리를 설명합니다.
> - Non-atomic Access: Java의 기본 데이터 타입 변수는 일반적으로 원자적으로 읽고 쓸 수 있지만, double과 long 타입은 일부 플랫폼에서 비원자적일 수 있습니다.
> - Word Tearing: 비원자적 접근은 단어 절단(word tearing)을 일으킬 수 있습니다. 이는 변수의 상위 32비트와 하위 32비트가 별도로 읽히거나 쓰여질 수 있음을 의미합니다.
> - Volatile Variables: volatile 키워드를 사용하면 double과 long 타입 변수의 원자적 접근을 보장할 수 있습니다.

> [!NOTE] JLS 17.4 - Shared Variables
> 이 섹션에서는 공유 변수와 그에 대한 동기화 규칙을 설명합니다.
> - **Shared Variables**: 모든 변수는 메모리에서 공유되며, 스레드는 변수의 값을 읽고 쓸 수 있습니다.
> - **Synchronization Actions**: 스레드 간의 상호작용은 동기화 작업을 통해 이루어지며, 이는 변수의 읽기와 쓰기뿐만 아니라 잠금 획득 및 해제와 같은 동기화 작업을 포함합니다.
> - **Program Order**: 각 스레드는 순차적으로 명령을 실행하지만, 다른 스레드가 이를 볼 때 순서가 달라질 수 있습니다.
> - **Happens-before**: 특정 작업이 다른 작업에 선행함을 보장하는 규칙입니다. 예를 들어, 하나의 스레드에서 동기화된 블록의 종료는 다른 스레드에서 동일한 블록의 진입보다 먼저 발생합니다.

아래 예시 코드를 보자

```java
public class StopThread {
    private static boolean stopRequested;
    public static void main(String[] args)
    throws InterruptedException {
        Thread backgroundThread = new Thread(() - > {
            int i = 0;
            while (!stopRequested)
                i++;
        });
        backgroundThread.start();
        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
    }
}
```

Main Thread 와 Background Thread 가 공유하는 변수 인 `stopRequested` 의 접근에 동기화가 이루어지지 않았다. Main Thread 에서 적용한 변경은 Background Thread 에 반영 되지  않을 수 있다.

이는 CPU cache, JIT Compile, reordering, `hoisting` 등의 기법이 JVM 레벨에서 발생하기 때문이다.

syncrhonized 키워드를 통해 stopRequested 의 접근을 동기화하면 의도 대로 동작한다.

```java
// Properly synchronized cooperative thread termination
public class StopThread {
    private static boolean stopRequested;
    private static synchronized void requestStop() {
        stopRequested = true;
    }
    private static synchronized boolean stopRequested() {
        return stopRequested;
    }
    public static void main(String[] args)
    throws InterruptedException {
        Thread backgroundThread = new Thread(() - > {
            int i = 0;
            while (!stopRequested())
                i++;
        });
        backgroundThread.start();
        TimeUnit.SECONDS.sleep(1);
        requestStop();
    }
}
```

한 쓰레드만이 Write 를 수행하고 다른 쓰레드는 Read 를 수행하는 상황이니 `volatile` 키워드 로도 충분하다!

```java
// Cooperative thread termination with a volatile field
public class StopThread {
    private static volatile boolean stopRequested;
    public static void main(String[] args)
    throws InterruptedException {
        Thread backgroundThread = new Thread(() - > {
            int i = 0;
            while (!stopRequested)
                i++;
        });
        backgroundThread.start();
        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
    }
}
```

즉, 아래와 같은 사례는 잘못 되었다.

```java
// Broken - requires synchronization!
private static volatile int nextSerialNumber = 0;
public static int generateSerialNumber() {
    return nextSerialNumber++;
}
```