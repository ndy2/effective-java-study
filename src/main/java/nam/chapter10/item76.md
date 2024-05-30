# Item 76 - 가능한 한 실패 원자적으로 만들라

호출된 메서드가 실패하더라도 해당 객체는 메서드 호출 전 상태를 유지해야 합니다. 이러한 특성을 failure-atomic 이라고 합니다. (부작용이 없는 함수)

### Q. Failure-Atomatic 을 만족하지 않는 Java 코드를 보여줘

A. 

```java
import java.util.ArrayList;
import java.util.List;

public class NonAtomicExample {
    private List<String> items = new ArrayList<>();

    public void addItems(List<String> newItems) {
        for (String item : newItems) {
            // This could throw an exception, e.g., if item is null
            if (item == null) {
                throw new IllegalArgumentException("Item cannot be null");
            }
            items.add(item);
        }
    }

    public List<String> getItems() {
        return items;
    }

    public static void main(String[] args) {
        NonAtomicExample example = new NonAtomicExample();
        List<String> newItems = List.of("item1", "item2", null, "item4");
        
        try {
            example.addItems(newItems);
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }

        // The state of 'items' is now partially modified
        System.out.println("Current items: " + example.getItems());
    }
}

```


### 불변 객체로 설계

- 불변 객체는 실패 원자적이다.
- 상태가 생성 시점에 고정되어 절대 변하지 않기 때문에, 실패에 따른 부작용이 없다.

### (호출하기 전) 매개변수의 유효성을 검사

```java
public Object pop() {
    if (size == 0)
        throw new EmptyStackException();
    Object result = elements[--size];
    elements[size] = null; // 다 쓴 참조 해제
    return result;
    }
```

- 배열 참조 전 배열의 크기가 0 이 아님을 검사

### 실패할 가능성이 있는 모든 코드를, 객체의 상태를 바꾸는 코드보다 앞에 배치

- 바로 위 사례 처럼 검사를 할 수 없는 경우에는 상태를 바꾸기 전 실패 하게 하자.
- 그러면 메서드가 실패하더라도 최소 상태가 변하지는 않는다.


### 객체의 임시 복사본으로 작업이 성공하면 원래 객체와 교체한다.

데이터를 임시 자료구조에 저장해 작업하는 게 더 빠를 때 적용하기 좋은 방법이다.

예를 들면, 어떤 정렬 메서드는 정렬하기 전 리스트의 값을 배열에 옮겨 담고 정렬을 수행한다. 배열을 사용하면 원소 접근이 빨라질뿐더러 정렬에 실패하더라도 기존 리스트는 변하지 않는 효과를 가질 수 있기 때문이다.

```java
import java.util.ArrayList;
import java.util.List;

public class AtomicExample {
    private List<String> items = new ArrayList<>();

    public void addItems(List<String> newItems) {
        List<String> tempList = new ArrayList<>(items);
        for (String item : newItems) {
            // This could throw an exception, e.g., if item is null
            if (item == null) {
                throw new IllegalArgumentException("Item cannot be null");
            }
            tempList.add(item);
        }
        // All operations succeeded, now update the original list
        items = tempList;
    }

    public List<String> getItems() {
        return items;
    }

    public static void main(String[] args) {
        AtomicExample example = new AtomicExample();
        List<String> newItems = List.of("item1", "item2", null, "item4");

        try {
            example.addItems(newItems);
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }

        // The state of 'items' remains unchanged
        System.out.println("Current items: " + example.getItems());
    }
}
```