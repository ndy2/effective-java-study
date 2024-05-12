# Item61 - 박싱된 기본 타입보다는 기본 타입을 사용하라

박싱된 기본 타입 (e.g. java.lang.Integer) 과 기본 타입에 관련된 개인적인 경험 두가지
## 1. JDBC

> [!QUOTE] java.sql.ResultSet#getInt
> - [openjdk > java.sql > ResultSet.getInt](https://github.com/openjdk/jdk/blob/master/src/java.sql/share/classes/java/sql/ResultSet.java#L301) 

java 의 오래된 인터페이스인 ResultSet 에서는 박싱된 기본 타입 대신 기본 타입을 활용하라는 본 책의 가이드를 따른 인터페이스 설계를 가진다.

`ResultSet` 인터페이스를 살펴보자.

```java
package java.sql;

...
/**
 * 데이터베이스 Result Set 을 표현하는 데이터 테이블.
 * 일반적으로 데이터베이스에 `statement` 를 이용해 쿼리를 수행한   * 결과로 생성된다. 
 */
public interface ResultSet extends Wrapper, AutoCloseable {

  /**
  * 마지막으로 읽은 column 이 SQL null 이었는지 반환한다.
  */
  boolean wasNull() throws SQLException;
  
  /**
  * column 값을 `String` 로 획득한다.
  * 
  * @return column 값; 값이 SQL `null` 이라면, null을 반환한다.
  */
  String getString(int columnIndex) throws SQLException;


  /**
  * column 값을 `int` 로 획득한다.
  * 
  * @return column 값; 값이 SQL `null` 이라면, 0을 반환한다.
  */
  int getInt(int columnIndex) throws SQLException;
  ...
}

```
null 이 될 수 있는 column 에 대해 getInt 를 호출한 경우 0을 반환하고 wasNull 로 반환값이 null 이었는지 확인해야한다.

처음 이 사실을 알았을때는 그냥 java.lang.Integer 를 사용하면 되지 않을까 하였으나 이 아이템을 보고나니 성능적인 관점에서 채택된 설계방식 이 아니었을까 생각한다.

## Kotlin Sort API

```kotlin
data class Data(  
    val a: Int,  
    val b: Int,  
)  
  
fun main() {  
    listOf(  
        Data(1001, 1002),  
        Data(1003, 1002),  
        Data(1002, 1002),  
        Data(1004, 1002),  
        Data(1006, 1002),  
        Data(1008, 1002),  
    )  
        .sortedBy { it.a }  
//        .sortedWith(compareBy { it.a })  
//        .sortedWith(Comparator { a, b -> compareValuesBy(a, b) { it.a } })  
//        .sortedWith(Comparator { a, b -> compareValues(a.a, b.a) })  
        .forEach { println(it) }  
  
    println("DONE")  
}
```

위 코드는 아주 정상적이다.
kotlin 의 정렬 api 를 이용해 주어진 Data 클래스를 a 의 크기 순으로 오름차순으로 정렬한다.

하지만 위 코드는 item61 을 준수하지 않았다.
그 비밀은 아래 compareValues 에 있다.

```kotlin
/**  
 * Compares two nullable [Comparable] values. Null is considered less than any value. 
 */  
public fun <T : Comparable<*>> compareValues(a: T?, b: T?): Int {  
    if (a === b) return 0  
    if (a == null) return -1  
    if (b == null) return 1  
  
    @Suppress("UNCHECKED_CAST")  
    return (a as Comparable<Any>).compareTo(b)  
}
```

sortedBy 를 호출하면 주석과 같은 절차를 거쳐 Comparator 를 생성하고 그 과정에서 compareValues 메서드를 활용한다. compareValues 메서드는 `nullable [Comparable] values` 를 비교한다.

이때 Int 로 선언된 Data 클래스의 a 필드에 AutoBoxing 이 발생하여 `java.lang.Integer` 인스턴스가 생성되고 불필요한 메모리가 낭비된다. InelliJ 의 debug 기능을 통해 compareValues 가 호출될 때 마다 `java.lang.Integer` 가 두개씩 생성 되는 모습을 볼 수 있다.

![[Pasted image 20240428130133.png]]

수정된 코드는 아래와 같다. Data 클래스 자체를 Comparable 을 구현함으로써 불필요한 Comparator 의 생성을 아예 하지 않는 방식이다. 혹은 compareValues 대신 AutoBoxing 이 발생하지 않는 방식으로 `sortedWith` 에 제공할 Comparator 를 직접 구현하여 넣어 주는 방식도 가능하다.

```kotlin
data class ComparableData(  
    val a: Int,  
    val b: Int,  
) : Comparable<ComparableData> {  
    override fun compareTo(other: ComparableData): Int {  
        val compareTo = a.compareTo(other.a)  
        return compareTo  
}  
}  
  
fun main() {  
    listOf(  
        ComparableData(1001, 1002),  
        ComparableData(1003, 1002),  
        ComparableData(1002, 1002),  
        ComparableData(1004, 1002),  
        ComparableData(1006, 1002),  
        ComparableData(1008, 1002),  
    ).sorted()  
        .forEach { println(it) }  
    println("DONE 274")  
}
```