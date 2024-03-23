# Item 48 - 스트림 병렬화는 주의해서 적용하라


> [!QUOTE] 핵심 정리
> 확신 없이는 스트림 파이프라인 병렬화를 사용하지 마라.

## 병렬화를 사용할 수 없는 경우

### 1. 요소가 순차적으로 연관을 가지는 경우

- 의미가 사라진다.

### 2. 데이터 소스가 Stream.iterate (무한 스트림)

- Stream.iterate  의 메서드 시그니처

```java
static <T> Stream<T> iterate(T seed, UnaryOperator<T> f)
```

- Stream.iterate 는 요소를 초기 값 부터 주어진 조건에 따라 반복적으로 생성한다.
- Stream.iterate 는 특히 정해진 규칙에 따라 무한한 스트림을 만들 때 유용하다.
- 하지만 무한한 스트림은 병렬화 시 주의가 필요.

무한한 정수 스트림을 생성하는 코드
```java
Stream<Integer> infiniteStream = Stream.iterate(1, i -> i + 1);
```

이 스트림을 어떻게 분할 할지 결정 할 수 없다.

### 2. 없다. - 중간 연산으로 limit 을 사용

- 병렬화는 limit 을 따룰 때 CPU 코어가 남는 다면, 원소를 몇 개 더 처리한 후 제한된 개수 이후의 결과를 버려도 아무런 해가 없다고 가정

```java
public static void main(String[] args) {  
    Stream.iterate(1, i -> {  
                int result = i + 1;  
                System.out.println("result : " + result);  
                return result;  
            })  
            .parallel()  
            .limit(5)  
            .forEach(System.out::println);  
}
```

```text
result : 2
result : 3
...
...
...
result : 3069
result : 3070
result : 3071
result : 3072
3
2
1
4
5
```

### 3. 있다. - 데이터 소스가 ArrayList, HashMap, HashSet, ConcurrentHashMap, 배열, int, long 범위

- 참조 지역성 (locality of reference)이 뛰어나다.
- 성능 향상에 도움이 될 수 있다.

## 병렬화는 성능 향상의 도구

- 적용에 따른 성능 향상을 측정하라.
- 머신러닝과 같은 데이터 처리 분야에서는 병렬화가 중요!

그럼에도... parallelStream 을 이용하지 않는다.

- 머신 러닝 (tensorflow/ pytorch)
- 데이터 처리 (apache spark/ flink)

등의 특화된 library/ framework 를 활용.
