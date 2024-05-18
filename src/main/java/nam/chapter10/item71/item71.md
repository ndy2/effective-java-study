# Item71 - 필요 없는 검사 예외 사용은 피하라

작가는 검사 예외를 잘 활용하면 안정성과 유연성을 가진 프로그램을 작성할 수 있다고 주장한다.

### **as-is**
**caller**
```java
try {
	File file = getFile("/path/to/file")
}catch (FileNotFoundException e) {
	// handle exception
}
```

**callee**
```java
File getFile(String path) throws FileNotFoundException{

	if(no file in `path`) {
		throw new FileNotFoundException("no file in " + path)
	}
	...
	return new File(..)
}
```

### **to-be** 검사 예외를 던지는 대신 빈 옵셔널을 반환한다.

**caller**
```java
File file = 
	getFile("/path/to/file").orElse(/* handle exception */)
```

**callee**
```java
Optional<File> getFile(String path) throws FileNotFoundException{

	if(no file in `path`) {
		Optional.empty()
	}
	...
	return Optional.of(new File(..))
}
```
**to-be** 검사예외가 발생할 수 있는 상황을 미리 확인한다.

```java
String path = "/path/to/file"
if (existsFile(path)){
	File file = getFile(path)
}else {
	// handle no such file
}

```

```java
boolean existsFile(String path) {
	return (file in `path`) as boolean
}

File getFile(String path) {
	...
	return new File(..)
}
```