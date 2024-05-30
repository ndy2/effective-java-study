# Item 73 - 추상화 수준에 맞는 예외를 던지라

수행하려던 일과 관련 없는 예외가 튀어나온다면 당황스러울 것이다.  메서드가 저수준 예외(구체화 단계의 예외)를 처리하지 않고 바깥으로 던져버리면 내부 구현 방식을 드러내어 윗 레벨 API 를 오염시킨다. 다음 릴리스에서 구현 방식을 바꾸면 다른 예외가 튀어나와서 기존 클라이언트 프로그램을 깨지게 할 수도 있는 것이다.

이 문제를 피하기 위해서는 **상위 계층에서 저수준 예외를 잡아 자신의 추상화 수준에 맞는 예외로 바꿔 던져야 한다**. 이를 `예외 번역(exception translation)` 이라고 한다. 

### Exception Translation

```java
try{
	...
} catch(LowerLevelException e){
	// 추상화 수준에 맞춘다
	throw new HigherLevelException(...);
}
```

### Exception Chaining

예외를 전환하면서 동시에 기존 예외를 전환된 예외의 생성자에 넣어 발생한 원인 (cause) 를 나타낼 수 있다.

```java
try{
	... // 저수준 추상화를 이용한다.
}catch(LowerLevelException cause){
	// 저수준 예외를 고수준 예외에 실어 보낸다.
    throw new HigherLevelException(cause);
}
```

### Surpressed Exception

https://www.baeldung.com/java-suppressed-exceptions

try-catch-finally 구문을 사용할때 finally block 예외를 던진다면 try 에서 던진 예외는 처리되지 않습니다. 

```java
public static void demoSuppressedException(String filePath) throws IOException {
    FileInputStream fileIn = null;
    try {
        fileIn = new FileInputStream(filePath);
    } catch (FileNotFoundException e) {
        throw new IOException(e);
    } finally {
        fileIn.close();
    }
}
```


존재하지 않는 파일을 열려고 시도하면 위 코드는 NullPointException 을 반환합니다.
fileIn 이 null 인채로 finally block 의 close 메서드를 호출했기 때문입니다.
```java
@Test(expected = NullPointerException.class)
public void givenNonExistentFileName_whenAttemptFileOpen_thenNullPointerException() throws IOException {
    demoSuppressedException("/non-existent-path/non-existent-file.txt");
}
```

아래의 finally block 에서는 첫번째 예외를 supressedException 으로 추가하였습니다.

```java
public static void demoAddSuppressedException(String filePath) throws IOException {
    Throwable firstException = null;
    FileInputStream fileIn = null;
    try {
        fileIn = new FileInputStream(filePath);
    } catch (IOException e) {
        firstException = e;
    } finally {
        try {
            fileIn.close();
        } catch (NullPointerException npe) {
            if (firstException != null) {
                npe.addSuppressed(firstException);
            }
            throw npe;
        }
    }
}
```

supressed exception 은 exception 에서 getCause 를 호출하여 chained 된 예외를 처리하는 것과 유사하게 던져진 예외에서 조회할 수 있습니다.

```java
try {
    demoAddSuppressedException("/non-existent-path/non-existent-file.txt");
} catch (Exception e) {
    assertThat(e, instanceOf(NullPointerException.class));
    assertEquals(1, e.getSuppressed().length);
    assertThat(e.getSuppressed()[0], instanceOf(FileNotFoundException.class));
}
```


#### Q.exception chaing 과의 차이는 뭐야? 그냥 cause 에다가 넣으면 안되?


A. 
exception chaining과 suppressed exception은 둘 다 예외 처리와 관련된 메커니즘이지만, 그 목적과 사용 방법에서 차이가 있습니다.

1. **사용 목적**:
    
    - **Exception Chaining**: 하나의 예외가 다른 예외의 원인임을 명시하고, 예외 발생의 전체적인 맥락을 보존하기 위해 사용.
    - **Suppressed Exception**: 주 예외와 별도로 자원을 해제하는 과정에서 발생한 예외를 보존하기 위해 사용.
2. **사용 상황**:
    
    - **Exception Chaining**: 여러 단계의 메서드 호출에서 예외를 전파하는 과정에서 주로 사용.
    - **Suppressed Exception**: 주로 try-with-resources 구문에서 자원을 해제하는 과정에서 발생하는 예외를 처리하기 위해 사용.
3. **구현 방식**:
    
    - **Exception Chaining**: 예외를 발생시킬 때 원인 예외를 생성자 또는 `initCause` 메서드로 설정.
    - **Suppressed Exception**: `addSuppressed` 메서드로 억제된 예외를 주 예외에 첨부하거나, try-with-resources 구문에서 자동으로 처리.

왜 그냥 cause에 넣지 않는가?

- **맥락의 분리**: Suppressed exception은 주 예외와 독립적으로 발생한, 특히 자원 해제 과정에서 발생한 예외를 명확히 구분합니다. 이를 통해 주 예외의 원인과 자원 해제 과정에서 발생한 예외를 혼동하지 않고 명확히 구분할 수 있습니다.
- **자동화**: try-with-resources 구문은 자동으로 억제된 예외를 처리하므로, 개발자가 일일이 예외를 추가하는 작업을 줄여줍니다.