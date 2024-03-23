# Item49 - 매개변수가 유효한지 검사하라


> [!QUOTE] 핵심 정리
> 매서드나 생성자를 작성할 때면 그 매개변수들에 어떤 제약이 있을지 생각해야 한다.


## 관련된 경험

### Server

#### As-Is

```
def someMethodV1(
	required field1 : Int,
	required field2 : String
){
	/*
		로직
	*/
}
```

#### To-Be

```
def someMethodV2(
	required dto: RequestDto
){
	// check dto.field1 exists 
	// check dto.field2 exists 

	/*
		로직
	*/
}

struct SomeStruct {
	optional field1 : Int,
	optional field2 : String
}
```

Server 개발 편의성을 위해 RequestDto 에 optional 을 도배함
이때 올바른 spec의 migration 을 위해 someMethod 의 dto.field1, dto.field2 가 required 라는 사실을 꼭 문서화 해 주어야 함.

### Client

#### As-Is

```
someServiceV1.someMethodV1(1, "hello")
```

#### To-Be

```
someServiceV2.someMethodV2(RequestDto(1, "hello"))

someServiceV2.someMethodV2(RequestDto(null, null)) // 왜 안돼?
```


## 이 원칙과 조금 헷갈리는 것


> [!QUOTE] 
> 주요 로직을 먼저 작성하고 예외 상황을 뒤에 작성해라

- 검증 (validation) 예외와 로직의 흐름에 따른 예외 (edge) 케이스 처리 로직을 처리하는 중 발생한 예외 (exception) 이 모두 대충 예외라는 말로 뭉뚱그려지기 때문에 헷갈린다고 생각함.
- 둘 다 맞는 말임