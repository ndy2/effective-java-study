# item56 - 공개된 API 요소에는 항상 문서화 주석을 작성하라


공개된 모든 클래스, 인터페이스, 메서드, 필드 선언에 문서화 주석을 달아야한다.

- 기본 생성자에는 문서화 주석을 달 방법이 없으니 공개 클래스는 절대 기본 생성자를 사용하면 안된다.

```java
public class Person{
	...

	// 아래 코드가 없다면 사용할 수 있는 new Person() 생성자에는 주석을 달 수 없다.
	// 필요하다면 factory method public 으로 제공할것
	public Person(String name, int age){
		this.name = name;
		this.age = age;
	}
}
```

## `Arrays.sort` 의 javadoc 일부

- `@throws` -  unchecked exception 을 선언하여 전제조건을 나타낸다.
- @param, @return - 인자와 반환값을 나타내는 태그


```java
/**  
 * Sorts the specified range of the array into ascending order. The range * to be sorted extends from the index {@code fromIndex}, inclusive, to  
 * the index {@code toIndex}, exclusive. If {@code fromIndex == toIndex},  
 * the range to be sorted is empty. * * @implNote The sorting algorithm is a Dual-Pivot Quicksort  
 * by Vladimir Yaroslavskiy, Jon Bentley, and Joshua Bloch. This algorithm * offers O(n log(n)) performance on all data sets, and is typically * faster than traditional (one-pivot) Quicksort implementations. 
 * 
 * @param a the array to be sorted  
 * @param fromIndex the index of the first element, inclusive, to be sorted  
 * @param toIndex the index of the last element, exclusive, to be sorted  
 * 
 * @throws IllegalArgumentException if {@code fromIndex > toIndex}  
 * @throws ArrayIndexOutOfBoundsException  
 *     if {@code fromIndex < 0} or {@code toIndex > a.length}  
 */
 public static void sort(int[] a, int fromIndex, int toIndex)
```

- 관례상 @throws 태그는 항상 if 으로 시작한다.
- 관례상 @param, @return, @throws 태그에는 마침표를 붙이지 않는다.


---
html 을 javadoc 에 넣을 수 있기 때문에 표를 넣을 수도 있고 심지어 이미지를 넣을 수도 있다.

![[javadoc-with-image.png]]

https://stackoverflow.com/questions/74456402/flux-javadoc-images-not-visible-in-reader-mode-intellij-visible-only-when-ho


Diagram 을 직관적으로 표현한 좋은 ~~Javadoc~~ ScalaDoc 예시

![[scaladoc-with-diagram.png]]

```scala
/**  
 * A [[Filter]] acts as a decorator/transformer of a [[Service service]].  
 * It may apply transformations to the input and output of that service: 
 * {{{  
 *           (*  MyService  *)  
 * [ReqIn -> (ReqOut -> RepIn) -> RepOut] 
 * }}}  
 * For example, you may have a service that takes `Strings` and  
 * parses them as `Ints`.  If you want to expose this as a Network  
 * Service via Thrift, it is nice to isolate the protocol handling * from the business rules. Hence you might have a Filter that * converts back and forth between Thrift structs. Again, your service * deals with plain objects: 
 * {{{  
 * [ThriftIn -> (String  ->  Int) -> ThriftOut]  
 * }}}  
 *  
 * Thus, a `Filter[A, B, C, D]` converts a `Service[C, D]` to a `Service[A, B]`.  
 * In other words, it converts a `Service[ReqOut, RepIn]` to a  
 * `Service[ReqIn, RepOut]`.  
 * 
 * @see The [[https://twitter.github.io/finagle/guide/ServicesAndFilters.html#filters user guide]]  
 *      for details and examples.  
 */
```

## JavaDoc 의 `@inheritDoc` 태그

java.util.Map 을 상속하여 주석을 상속

![[Pasted image 20240414131751.png]]


![[Pasted image 20240414131754.png]]

