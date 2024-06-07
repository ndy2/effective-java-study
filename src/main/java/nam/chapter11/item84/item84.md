# Item 84

정확성을 보장하거나 성능을 높이기 위해 스레드 스케줄러에 의존하지 말라

## CountDownLatch

- 하나 이상의 스레드가 다른 스레드에서 수행되는 일련의 작업이 완료될 때 까지 기다릴 수 있게 해주는 동기화 보조 도구
- 주어진 카운트로 초기화되고 await 메서드는 현재 카운트가 `countDown` 메서드의 호출로 인해 0이 될 때 까지 블록되며 그 이후에 모든 대기 중인 스레드가 해제되고 await() 이후 처리가 이루어진다.


![[countdownlatch.excalidraw.png]]


## CyclicBarrier

공통된 장벽 지점에 도달할 때까지 일련의 스레드가 서로 기다리도록 하는 동기화 보조 도구


![[cyclicbarrier.excalidraw.png]]

![[vs.png]]


CountDownLatch 가 busywait 없이 awiat 를 구현한 방법

- **CountDownLatch 클래스 구조**: `CountDownLatch`는 내부적으로 AQS(AbstractQueuedSynchronizer)를 사용합니다. AQS는 Java의 동시성 라이브러리에서 다양한 동기화 기법을 구현하기 위해 사용되는 추상 클래스입니다.

- **await() 메소드**: `await()` 메소드는 `state`가 0이 될 때까지 대기합니다. 이 메소드는 busy wait를 사용하지 않고, 대기 중인 스레드를 효율적으로 관리합니다. 아래는 주요 동작 원리입니다.

- `state` 값이 0이 아니면, 현재 스레드는 대기 상태로 전환됩니다.
- AQS의 `acquireSharedInterruptibly` 메소드를 통해 공유 락을 획득하려 시도합니다.
- AQS는 대기 큐(Condition Queue)를 사용하여 대기 중인 스레드를 관리합니다. 대기 상태의 스레드는 CPU 자원을 소모하지 않습니다.