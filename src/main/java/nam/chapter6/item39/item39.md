# Item39 명명 패턴 보다 애너테이션을 사용하라

> Prefer annotations to naming pattern

## Notes

**명명 패턴**

- 명명 패턴을 한글로 검색해보면 이 책의 내용 밖에 나오지 않는다. 그 만큼 잘 사용되지 않고 패턴으로서의 가치가 없기 때문이라고 생각한다.
- 책에서도 딱히 정의 없이 바로 `명명 패턴`이라고 이야기 하는 것이 내용을 보면 바로 어떤 패턴을 말하는지 알 수 있기 때문이다.
- 이름을 통해 명시적이지 않은 방법으로 메서드 혹은 클래스 등에 부가적인 기능을 추가하는 패턴이다. 

**애너테이션**

- Java Annotation 은 코드에 대한 메타데이터이다.
- class, method, field 등 다양한 위치에 활용 될 수 있다.

## 명명 패턴의 단점

- 오타가 나면 안된다.
- 코드를 작성하는 측과 활용하는 측에 명시적이지 않은 규약이 필요해진다.
- 부가적인 기능을 위한 인자를 전달하는것에 제한적이고, 아름답지 않다.

## 프로젝트에서 Reapeatable Annotation 을 활용한 경험

Swagger 에서 기본적으로 모든 API 에 400/404/500 에 대한 예외 응답을 공통되게 문서화로 추가하는 것이 마음에 들지 않아서 @ErrorResponseApi 를 도입하여 컨트롤러에서 활용한 적이 있음.

```java
// ErrorResponseApi.java
/**
 * Controller Method 에 추가하여 해당 메서드에서 발생할 수 있는 예외를 문서화 하기위해 사용
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ErrorResponseApis.class)
public @interface ErrorResponseApi {

	@AliasFor("messages")
	ErrorResponseWithMessages value() default BAD_REQUEST;

	@AliasFor("value")
	ErrorResponseWithMessages messages() default BAD_REQUEST;

	String[] args() default {};
}

// ErrorResponseApis.java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ErrorResponseApis {

	ErrorResponseApi[] value();
}
```

```java
@Component
public class CustomOperationCustomizer implements OperationCustomizer {

	private final String mediaType;
	private String errorResponseRef;

	public CustomOperationCustomizer(SpringDocConfigProperties properties) {
		this.mediaType = properties.getDefaultProducesMediaType();
	}

	@Override
	public Operation customize(Operation operation, HandlerMethod handlerMethod) {
		ApiResponses apiResponses = operation.getResponses();
		// extract reference for errorResponse schema
		if (errorResponseRef == null) {
			errorResponseRef = apiResponses.get("400").getContent().get(mediaType).getSchema().get$ref();
		}

		// remove default error responses
		apiResponses.remove("400");
		apiResponses.remove("404");
		apiResponses.remove("500");

		// add example error responses in customized way
		Set<ErrorResponseApi> errorResponseApis =
			AnnotatedElementUtils.findMergedRepeatableAnnotations(handlerMethod.getMethod(), ErrorResponseApi.class);
		errorResponseApis.forEach(errorResponseApi -> addErrorResponseApi(apiResponses, errorResponseApi));

		return operation;
	}

	private void addErrorResponseApi(ApiResponses apiResponses, ErrorResponseApi errorResponseApi) {
		...
	}
}
```

컨트롤러에서의 활용

```java
@Slf4j
@RequiredArgsConstructor
@Tag(name = "signup", description = "회원가입과 관련된 endpoint 모음")
@RequestMapping("/signup")
@RestController
public class SignUpController {

	private final UserService userService;

	@Operation(
		summary = "회원 가입",
		description = "이메일, 비밀번호를 이용한 회원 가입"
	)
	@ErrorResponseApi(SIGNUP_BAD_REQUEST)
	@PostMapping
	public SignUpResponse signUp(
		@RequestBody SignUpRequest request
	) {
		return userService.signUp(request);
	}
	...
}
```

Springframework 는 애너테이션의 활용을 도와주는 유틸 클래스 두가지를 제공한다.

- `AnnotationUtils`
- `AnnotatedElementUtils` - `AnnotationUtils` 보다 더 확장된 다양한 기능을 제공한다.

## 다른 언어에서의 명명 패턴

오늘날은 보기 힘든 패턴이지만 python 의 `pytest` 는 책에서 다룬 과거의  `junit` 과 동일한 방식으로 test prefix 를 기반으로 동작한다.

또한 JNI (Java Native Interface) 를 활용하기 위한 c 코드는 정해진 이름 규칙에 따라 함수를 정의해야 한다. 

```c
#include <stdio.h>
#include "HelloJNI.h"

JNIEXPORT void JNICALL Java_HelloJNI_sayHello(JNIEnv *env, jobject obj) {
    printf("Hello, JNI World!\n");
    return;
}
```

## 사실...

이 책이 작성되던 시점에 비해 현재는`spring`, `lombok`, `jackson` 등 다양한 프레임워크에서 훨씬 동적인 방향으로 애너테이션을 다이나믹하게 활용하고 있다.

