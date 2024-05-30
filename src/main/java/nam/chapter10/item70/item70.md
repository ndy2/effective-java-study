# Item70 - 복구할 수 있는 상황에는 검사 예외를, 프로그래밍 오류에는 런타임 예외를 사용하라

호출하는 쪽에서 복구하리라 여겨지는 상황이라면 CheckedException 을 사용하라.

![[java-exception-hierachy.png]]

CheckedException 이란 RuntimeException 을 상속하지 않은 예외로써 항상 명시적으로 처리되어야 한다. 즉, 호출하는 측에서

- try-catch 블락으로 처리되거나
- 명시적으로 throws 를 통해 메서드 선언부에 추가 되어야 한다.

p.s. Checked Exception 을 아예 사용하지 않고 모든 예외를 Unchecked Exception 으로 처리하는 것이 대세가 되었다.

Examples of Checked Exception
- `IOException`
- `SQLException`
- `FileNotFoundException`
- `ClassNotFoundException`

	이 예외들이 과연 Exception Handling 을 통해 복구 될 수 있는 예외인지는 생각해볼 문제이다. 또한 복구 가능 유무가 명시적인 핸들링 구문 포함을 강제해야 하는지도 많은 개발자들이 의문을 가지고 있다. 