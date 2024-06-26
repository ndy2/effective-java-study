# Item 40 - `@Override` 애너테이션을 일관되게 사용하라

> Consistently use the `Override` annotation

## Study with ChatGPT

Q : 재정의를 했지만 @Override 를 안 써도 되?

> `@Override` 어노테이션은 메소드가 부모 클래스 또는 인터페이스에서 상속받은 메소드를 오버라이드하고 있는지를 컴파일러에게 알려주는 역할을 합니다. 이 어노테이션을 사용하면 다음과 같은 장점이 있습니다:
> 1. **컴파일러 오류 방지:** 만약 `@Override` 어노테이션을 사용하지 않고 재정의하려는 메소드의 시그니처를 실수로 변경하면, 컴파일러가 이를 감지하고 오류를 발생시킵니다. 이는 코드의 안정성을 높여줍니다.
> 2. **가독성 향상:** `@Override` 어노테이션을 사용하면 코드를 읽는 사람에게 해당 메소드가 부모 클래스 또는 인터페이스에서 상속받은 것임을 명시적으로 알려줍니다.
> 3. **리팩토링 지원:** 코드를 변경하고자 할 때, 이전 버전의 메소드를 삭제하거나 시그니처를 변경하는 경우, 해당 메소드에 `@Override` 어노테이션이 있는 경우 컴파일러가 이를 감지하여 수정이 필요한 부분을 알려줍니다.
> 
> 따라서, `@Override` 어노테이션을 사용하는 것은 좋은 프로그래밍 관행 중 하나입니다. 그러나 자바 5 버전 이후부터는 컴파일러에서 체크하는 기능이 제공되기 때문에, 반드시 필수적으로 사용해야 하는 것은 아닙니다.
> 
> 결론적으로, `@Override` 어노테이션은 코드의 가독성과 유지보수성을 높이는데 도움이 되므로 사용하는 것이 좋습니다.

**java.lang.Override.java**

```java
package java.lang;  
  
import java.lang.annotation.*;

@Target(ElementType.METHOD)  
@Retention(RetentionPolicy.SOURCE)  
public @interface Override {  
}
```