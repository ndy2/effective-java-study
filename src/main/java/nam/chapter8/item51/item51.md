# Item51 - 메서드 시그니처를 신중히 설계하라

## Read the Orcale Docs

https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html

메서드 선언은 아래 여섯 구성요소로 이루어진다.

1. 접근 제어자 (Modifiers) - e.g. public, private ...
2. 반환 타입 (Return Type)
3. 메서드 이름 (Method Name)
4. 파라미터 목록 (Parameter list)
5. 예외 목록 (Exception list)
6. 메서드 바디 (Method Body)

메서드 선언의 구성 요소중 `메서드 이름`과 `파라미터 목록` 을 합쳐 `메서드 시그니처` 라고 한다.

> [!QUOTE] Baeldung - Method Signature
> https://www.baeldung.com/java-method-signature-return-type
> 위 문서에서는 여섯 요소 뿐만 아니라 제네릭 타입인자, 가변인자 등이 메서드 시그니처와 어떻게 동작하는지 설명한다.
## 메서드 시그니처 설계의 가이드

1. 이름은 신중히 정하라
2. 편의 메서드를 너무 많이 만들지 말라
3. 매개변수 목록을 짧게 유지하라
4. 매개변수 타입으로는 클래스보다 인터페이스가 낫다