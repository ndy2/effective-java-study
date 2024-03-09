
> [!quote] 핵심 정리
>  가능하면 컬렉션을 반환해라
> 
> 아래의 순서대로 생각해라
> 1. 표준 컬렉션
> 2. 전용 컬렉션
> 3. Iterable or Stream

> [!quote] 참고 자료
> Baeldung - [Returning Stream vs Collection](https://www.baeldung.com/java-return-stream-collection)

책의 내용을 보면 무조건 Collection 을 사용하는 것이 좋은 것으로 보인다. 어떤 경우에 Stream 을 사용하는 것이 좋은 선택이 될 수 있는지 Baeldung 문서를 통해 알아보자.

## 3. When to Return a _Stream_?

### 3.1. 구체화 비용이 클때

메모리에 올리는 비용이 큰 경우에는 Stream 이 좋은 선택이다. Stream 의 lazy/ short curcit 특성은 이런 구체화 비용 절감에 큰 도움이 된다.

예를 들어 Java NIO `Files` 클래스의 `readAllLines` 메서드는 파일의 모든 lines 를 읽어 메모리로 읽어 들인다. 이는 파일이 매우 큰 경우 OOM 을 유발 할 수 있다.

반면 `lines` 메서드는 Stream 을 응답하므로 메모리 부담에서 상대적으로 자유롭다.

### 3.2. 결과가 많거나 무한할때

> [!quote] 참고 자료
> Baeldung - [Java Infinite Streams](https://www.baeldung.com/java-inifinite-streams)
> 케빈 - [모던 자바 (자바8) 못다한 이야기 - 08 Stream API - 03 Stream API 01 - 무한 collection](https://youtu.be/oaKTK58qI30)

## 4. When to Return a _Collection_?

### 4.1. 구체화 비용이 낮을때

Collection 은 Stream 과 달리 eagerly 하게 heam memory 를 차지한다.

### 4.2. Collection 의 고정된 응답 형태 (순서) 가 필요할때

TreeSet 과 같은 응답이 필요한 경우를 의미한다.

### 4.3. 재사용성이 필요할 때

Stream 은 한번 사용하면 다시 사용할 수 없다. 재사용이 필요하면 Collection 을 이용하자.

### 4.4. 변경 (modification) 이 필요할 때

Stream 은 Collection 과 달리 변경 할 수 없다. 변경이 필요하면 Collection 을 이용하자.

### 4.5. In-Memory Result

이미 메모리에 올라와 있는 경우라면 Collection 을 그냥 응답해라.
