# Item 46 - 스트림에서는 부작용 없는 함수를 사용하라

> [!quote] 핵심 정리
> 스트림 파이프라인 프로그래밍의 핵심은 부작용 없는 함수 객체에 있다.
> 
> - 종단 연산 중 forEach 는 스트림이 수행한 계산 결과를 보고할 때만 이용한다.
> - 스트림을 올바르게 사용하려면 수집기 (Collectors) 를 알아야 한다.
> 	- 가장 중요한 Collector Factory 는 toList, toSet, toMap, groupingBy, joining 이다.

> [!quote] 참고 자료
> 라울-게이브리얼 우르마 외 2인 - `모던 자바 인 액션`
## 스트림을 이해하는 자세 (?)

- 스트림은 그저 또 하나의 API 가 아니라, 함수형 프로그래밍에 기초한 패러다임 이다.
- API 를 단순하게 활용하는 것으로는 스트림의 진정한 효과를 볼 수 없다.

### 스트림의 주는 다양한 이득

- 선언형 (declarative way) 으로 코드를 구현할 수 있다.
	- 즉, 루프와 if 조건문 등의 제어 블록을 사용해서 어떻게 동작을 구현할지 지정할 필요가 없다.
- 유연성 - 이를 통해 여러 빌딩 블록 연산을 연결해 조립할 수 있다.
	- 변하는 요구 사항에 더 쉽게 대응 할 수 있다.
- 병렬화
	- 성능이 좋아진다.


## 스트림과 side-effect

### Study with ChatGPT

Q : java 의 stream 에서 side-effect 가 있는 함수를 사용하면 안되는 이유를 알려줘

Java의 Stream API에서 side-effect(부작용)가 있는 함수를 사용하면 안 되는 이유는 몇 가지가 있습니다.

1. **Immutability와 Functional Programming 원칙 위배:**
    - Stream API는 함수형 프로그래밍의 원칙을 따르며, 데이터를 변경하지 않는 불변성(Immutability)을 중요시합니다. Side-effect가 있는 함수는 데이터를 변경할 수 있으므로, 이러한 원칙을 위배하게 됩니다.

2. **병렬 처리의 어려움:**
    - Stream API는 병렬 처리를 효과적으로 지원합니다. 하지만 side-effect가 있는 함수를 사용하면 이 병렬 처리를 어렵게 만들 수 있습니다. 여러 스레드에서 동시에 동일한 데이터를 변경하면 예측할 수 없는 결과가 발생할 수 있습니다.

3. **디버깅의 어려움:**
    - Side-effect가 있는 함수는 코드의 상태를 변경하므로, 디버깅이 어려워집니다. Stream API를 사용하는 것은 코드를 간결하게 만들 수 있는데, side-effect를 도입하면 코드의 상태를 이해하고 추적하기 어려워집니다.

4. **가독성 및 유지보수 어려움:**
    - Side-effect는 코드의 의도를 명확히 전달하지 않을 수 있습니다. 코드를 읽는 사람이나 유지보수하는 개발자가 코드를 이해하기 어려워질 수 있습니다. Stream API를 사용하면 선언적인 스타일로 코드를 작성할 수 있어 가독성이 향상됩니다.

따라서 Stream API를 사용할 때는 side-effect가 없는 순수 함수(pure function)를 사용하는 것이 좋습니다. 이를 통해 코드의 가독성, 유지보수성을 높이고, 병렬 처리와 같은 성능 이점을 최대한 활용할 수 있습니다.

---

2. **병렬 처리의 어려움** 예시

1 ~ 100 까지 숫자의 합을 parallelStream 을 써서 멋지게 구해보자.

```java
public class SideEffectExample1 {  
  
    private static int total = 0;  
  
    public static void main(String[] args) {  
        List<Integer> numbers = IntStream.range(1, 100)  
                .boxed()  
                .collect(Collectors.toList());  
  
        // Side-effect가 있는 코드 (나쁜 예)  
        numbers.parallelStream()  
                .peek(n -> total += n)  // 부작용: total 값을 변경  
                .forEach(System.out::println);  
  
        System.out.println("Total: " + total);  // 부작용 결과 출력  
    }  
}
```

정답은 당연히 5050 이다.

```
65
66
.
.
.
27
Total: 4934
```

---

3.  **디버깅의 어려움** 예시

200 칼로리가 넘는 메뉴를 필터링해 이름을 담아보자.
그 과정에 디버깅을 위해 콘솔에 로그를 출력해본다.

```java
public class SideEffectExample2 {  
  
    public static void main(String[] args) {  
  
        List<Dish> menus = List.of(  
                new Dish("beer", 300),  
                new Dish("apple", 10),  
                new Dish("curry", 250),  
                new Dish("pie", 100),  
                new Dish("sushi", 300)  
        );  
  
        List<String> names = menus.stream()  
                .filter(dish -> {  
                    System.out.println("filtering: " + dish.name);  
                    return dish.calories > 200;  
                })  
                .map(dish -> {  
                    System.out.println("mapping: " + dish.name);  
                    return dish.name;  
                })  
                .limit(2)
                .collect(Collectors.toList());  
  
        System.out.println(names);  
    }  
  
    record Dish(String name, int calories) { }  
}
```

어찌된 일인지 로그가 생각한 순서와 다르다.
이런식으로 스트림을 잘 모르고 활용만 한다면 디버깅에 어려움을 줄 수 있다.

```
filtering: beer
mapping: beer
filtering: apple
filtering: curry
mapping: curry
[beer, curry]
```

한편 그 이유는 ...

> [!quote]
> Intermediate operations return a new stream. They are always lazy; 

## Collectors/ Collector

### Collector 란 무엇인가?

Collector 인터페이스 구현은 스트림의 요소를 어떤 식으로 도출할지 지정한다.

- 이전 예제에서 collect 메서드로 Collecor 인터페이스 구현을 전달했다.
- 이전 예제에서 '각 요소를 리스트로 만들어라' 를 의미하는 toList 를 Collector 인터페이스의 구현으로 활용했다.

![[collector.png]]

### Collectors 는?

미리 정의된 Collector 를 제공하는 팩토리 클래스.
Collectors 에서 제공하는 메서드의 기능은 크게 세 가지로 구분할 수 있다.

- 스트림 요소를 하나의 값으로 리듀스하고 요약
- 요소 그룹화
- 요소 분할

|                       |                                                |                                                                              |
| --------------------- | ---------------------------------------------- | ---------------------------------------------------------------------------- |
| 팩토리 메서드               | 반환 형식                                          | 설명                                                                           |
| **toList**            | `List<T>`                                      | 스트림의 모든 항목을 리스트로 수집                                                          |
| **toSet**             | `Set<T>`                                       | 스트림의 모든 항목을 중복이 없는 집합으로 수집                                                   |
| **toCollection**      | `Collection<T>`                                | 스트림의 모든 항목을 발행자가 제공하는 컬렉션으로 수집                                               |
| **counting**          | `Long`                                         | 스트림의 항목 수 계산                                                                 |
| **summingInt**        | `Integer`                                      | 스트림의 항목에서 정수 프로퍼티값을 더함                                                       |
| **averagingInt**      | `Double`                                       | 스트림 항목의 정수 프로퍼티의 평균값 계산                                                      |
| **summarizingInt**    | `IntSummaryStatistics`                         | 스트림 내 항목의 최댓값, 최솟값, 합계, 평균 등의 정수 정보 통계 수집                                    |
| **joining**           | `String`                                       | 스트림의 각 항목에 toString 메서드를 호출한 결과 문자열 연결                                       |
| **maxBy**             | `Optional<T>`                                  | 주어진 비교자를 이용해서 스트림의 최댓값 요소를 Optional로 감싼 값을 반환                                |
| **minBy**             | `Optional<T>`                                  | 주어진 비교자를 이용해서 스트림의 최솟값 요소를 Optional로 감싼 값을 반환                                |
| **reducing**          | The type produced by the reduction operation   | 누적자를 초깃값으로 설정한 다음에 BinaryOperator로 스트름의 각 요소를 반복적으로 누적자와 합쳐 스트림을 하나의 값으로 리듀싱 |
| **collectingAndThen** | The type returned by the transforming function | 다른 컬렉터를 감싸고 그 결과에 반환 함수 적용                                                   |
| **groupingBy**        | `Map<K, List<T>>`                              | 하나의 프로퍼티값을 기준으로 스트림의 항목을 그룹화하며 기준 프로퍼티값을 결과 맵의 키로 사용                         |
| **partitioningBy**    | `Map<Boolean, List<T>>`                        | Predicate를 스트림의 각 항목에 적용한 결과로 항목 분할                                          |

> -  가장 중요한 Collector Factory 는 toList, toSet, toMap, groupingBy, joining 이다.

**toList** 
`List<Dish> dishes = menuStream.collect(toList())`

**toSet** 
`Set<Dish> dishes = menuStream.collect(toSet())`

**toMap** 
`Map<Integer, String> caloryToName 
`= menuStream.collect(toMap(Menu::getCalories, Menu::getName))`

**groupingBy** 
`Map<Dish.Type, List<Dish>> dishesByType = menuStream.collect(groupingBy(Dish::getType))`

**joining**
`String shortMenu = menuStream.map(Dish::getName).collect(joining(",")`