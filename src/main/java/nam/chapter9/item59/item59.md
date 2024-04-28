# Item59 - 라이브러리를 익히고 사용하라


> [!QUOTE] 참고자료
> •  bauldung > [New Features in Java 8](https://www.baeldung.com/java-8-new-features)  
> •  bauldung > [New Features in Java 9](https://www.baeldung.com/new-java-9)  
> •  bauldung > [New Features in Java 10](https://www.baeldung.com/java-10-overview)  
> •  bauldung > [New Features in Java 11](https://www.baeldung.com/java-11-new-features)  
> •  bauldung > [New Features in Java 12](https://www.baeldung.com/java-12-new-features)  
> •  bauldung > [New Features in Java 13](https://www.baeldung.com/java-13-new-features)  
> •  bauldung > [New Features in Java 14](https://www.baeldung.com/java-14-new-features)  
> •  bauldung > [New Features in Java 15](https://www.baeldung.com/java-15-new)  
> •  bauldung > [New Features in Java 16](https://www.baeldung.com/java-16-new-features)  
> •  bauldung > [New Features in Java 17](https://www.baeldung.com/java-17-new-features)
> • wikipedia > https://en.wikipedia.org/wiki/Java_version_history
> • geeksforgeeks > https://www.geeksforgeeks.org/java-jdk-21-new-features-of-java-21/
> • openjdk > jeps - https://openjdk.org/jeps/0

![[Pasted image 20240421134802.png]]

약간 잔소리 같은 이번장..

Java 의 새로운 버전이 출시되어도 (어짜피 사용하지도 않아서) 관심도 별로 없고 디테일한 API 및 라이브러리 내부 보다는 겉으로 드러나는 **Language Features** 에 더 관심이 많이 가는것 같음

- **Language Features** 는 딱히 공부 할 게 없다. 보고 쓰면 됨

## 1. Language Features

java 11 - var/val
java 14 - record, switch expression
java 17 - switch pattern matching,  sealed classes and interfaces
java 21 
- no more psvm - https://openjdk.org/jeps/445
- string template (preview) - https://openjdk.org/jeps/430
- Unnamed Patterns and Variables - https://openjdk.org/jeps/443

## 2. Library Features

java 21
- **Virtual threads** - https://openjdk.org/jeps/444 
	- 꼭 한번 알아보자.
	- https://wiki.openjdk.org/display/loom/Main
- Sequenced collection - https://openjdk.org/jeps/431

## 3. Performance Improvements

java 21 
- Z Garbage Collection - https://openjdk.org/jeps/439