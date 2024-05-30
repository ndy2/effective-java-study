# Item68 - 일반적으로 통용되는 명명 규칙을 따르라


> [!QUOTE] 참고 자료
> - 자바 언어 명세 [JLS, 6.1](https://docs.oracle.com/javase/specs/jls/se7/html/jls-6.html#jls-6.1)
> - NAVER D2 - [영어 변수명을 잘 지어보자](https://youtu.be/rbSnkiqPnJI) (IOS 앱 개발 관련 예제)
> - 포프TV - [올바른 변수 이름 짓는 법](https://youtu.be/ZtkIwGZZAq8) 
> - 얄팍한 코딩사전 - [변수명 함수명 ...](https://youtu.be/-GQLJSpiE2M)

## 철자

**패키지**

- 다른 패키지와 구분되는 unique 한 이름을 부여 할 수 있도록 하자.
- 인터넷 도메인을 역순으로 작성하는 방식을 권장한다.
	- 도메인 이름에 hyphen 등의 특수 문자는 underscore 로 치환
	- `keyword` 가 포함된 경우는 underscore 를 뒤에 붙인다.
	- 첫 문자에 숫자 등의 사용할 수 없는 문자가 없는 경우 underscore 를 앞에 붙인다.

> [!EXAMPLE] 일반 적인 패키지 이름 예시
> - com.nighthacks.java.jag.scrabble
> - org.openjdk.tools.compiler
> - net.jcip.annotations
> - edu.cmu.cs.bovik.cheese
> - gov.whitehouse.socks.mousefinder

> [!EXAMPLE] undercore 가 필요한 패키지 이름 예시
> - `com.example_site`
> - `com.record_.extends_`
> - `com._123example`
> - `com.keyword_123site`

- 예외적으로 표준 라이브러리는 java/javax 로 시작한다. 

**클래스/인터페이스**

- 클래스의 이름은 명시적인 명사/ 명사 구로 작성되어야 한다. 너무 길면 안되고 **PascalCase** 로 작성한다.

> [!EXAMPLE] Descriptive Class Names
> - ClassLoader
> - SecurityManager
> - Thread
> - Dictionary
> - BufferedInputStream

- 인터페이스의 이름도 짧고 명시적인 PascalCase 로 작성되어야 한다. 명사/명사구로 적절한 역할을 드러낼 수 있는 경우 명사로 형용사로써 행위를 드러내는것이 적절한 경우는 형용사로 작성한다.

> [!EXAMPLE] 인터페이스 이름 예시
> `java.io.DataInput` - 명사
> `Runnable`, `Cloneable` - 형용사

- p.s.
![[logestClassNameInJava.png]]

**메서드와 필드**
- remove, groupingBy, getCrc

**상수 필드**
- MIN_VALUE, NEGATIVE_INFINITY

**지역 변수**
- i, denom, houseNum

**타입 매개변수**
- T, E, K, V, X, R, U, V, T1, T2
## 문법

- 철자로 결정된 단어를 어떻게 사용할지 결정하는 것
- e.g.) 단수/복수, 명사/동사/형용사

## 자주 사용하는 패턴

- **Getter/ Setter**
- **toType**

## 포프

- 영어를 모국어로 쓰지 않는 입장에서 특히 더 신경 써야한다.
- 변수 이름에는 동사를 쓰지 말자
- 단수일때 관사를 신경 쓰지 말자
	- `aUser` -> `user`
	- `userList` 도 쓰지만 `users`가 더 통용되는 방식
- 전치사도 쓰지 말자, 짧고 간결하게!
	- numberOfUser -> numberUser -> userCount, numUsers -> userCnt


## 네이버 

scope 이 한정된 경우 중복을 줄이자.
```java
class User {
	private long id; 
	// private long userId; 
}

user.id
user.userId
```
메서드의 짝을 맞추자
- show/hide
- present/dismiss
- start/stop
