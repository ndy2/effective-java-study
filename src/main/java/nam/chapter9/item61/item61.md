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

