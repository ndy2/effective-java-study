# Item 50 - 적시에 방어적 복사본을 만들라

> [!QUOTE] 핵심 정리
> 클래스가 클라이언트로부터 받는 혹은 클라이언트로 반환하는 구성요소가 가변이라면 그 요소를 방어적 복사하라. 혹은 관련 책임을 명확히 문서화 해라.


## 반환 시에 방어적 복사본을 활용해야 한다.

**Map 으로 근무 일자를 관리하는 클래스**

```java
class DayOfWorkByEmployee {  
  
    Map<String, Set<Date>> map = new HashMap<>();  
  
    void addWorkDate(String name, Date date) {  
        if (map.containsKey(name)) {  
            map.get(name).add(date);  
        } else {  
            HashSet<Date> workDates = new HashSet<>();  
            workDates.add(date);  
            map.put(name, workDates);  
        }  
    }  
  
    Set<Date> getWorkDates(String name) {  
        return map.getOrDefault(name, new HashSet<>());  
    }  
}
```

**pjp 가 ndy 의 근무일자를 멋대로 제거할 수 있다!**

```java
public static void main(String[] args) {  
    DayOfWorkByEmployee dayOfWorkByEmployee = new DayOfWorkByEmployee();  
  
    // ndy log his work dates  
    String ndy = "ndy";  
    List<Date> ndyWorkDates = List.of(  
            new Date(2024, 3, 25),  
            new Date(2024, 3, 26),  
            new Date(2024, 3, 27),  
            new Date(2024, 3, 28),  
            new Date(2024, 3, 29)  
    );  
    ndyWorkDates.forEach(it -> dayOfWorkByEmployee.addWorkDate(ndy, it));  
    System.out.println("ndy work days : " + dayOfWorkByEmployee.getWorkDates(ndy));  
  
    // pjp remove ndy's work dates  
    dayOfWorkByEmployee.getWorkDates(ndy).clear();  
  
    // ndy check his work log again  
    System.out.println("ndy work days : " + dayOfWorkByEmployee.getWorkDates(ndy));  
}
```

```
ndy work days : [Sat Apr 26 00:00:00 KST 3924, Mon Apr 28 00:00:00 KST 3924, Fri Apr 25 00:00:00 KST 3924, Sun Apr 27 00:00:00 KST 3924, Tue Apr 29 00:00:00 KST 3924]
ndy work days : []
```

방어적 복사본을 이용해 반환시 내부 컬렉션이 아닌 별도의 컬렉션을 복사하여 반환한다.

```java
Set<Date> getWorkDates(String name) {  
    return new HashSet<>(map.getOrDefault(name, new HashSet<>()));  
}
```

```
ndy work days : [Sat Apr 26 00:00:00 KST 3924, Mon Apr 28 00:00:00 KST 3924, Fri Apr 25 00:00:00 KST 3924, Sun Apr 27 00:00:00 KST 3924, Tue Apr 29 00:00:00 KST 3924]
ndy work days : [Sat Apr 26 00:00:00 KST 3924, Mon Apr 28 00:00:00 KST 3924, Fri Apr 25 00:00:00 KST 3924, Sun Apr 27 00:00:00 KST 3924, Tue Apr 29 00:00:00 KST 3924]
```

## 클라이언트로 부터 받는 요소에 방어적 복사본을 활용해야 한다.

**ndy 가 멋대로 근무일자를 수정하여 주말 출근 기록을 생성한다.**

```java
public static void main(String[] args) {  
    DayOfWorkByEmployee dayOfWorkByEmployee = new DayOfWorkByEmployee();  
  
    // ndy log his work dates  
    String ndy = "ndy";  
    List<Date> ndyWorkDates = List.of(  
            new Date(2024, 3, 25),  
            new Date(2024, 3, 26),  
            new Date(2024, 3, 27),  
            new Date(2024, 3, 28),  
            new Date(2024, 3, 29)  
    );  
    ndyWorkDates.forEach(it -> dayOfWorkByEmployee.addWorkDate(ndy, it));  
    System.out.println("ndy work days : " + dayOfWorkByEmployee.getWorkDates(ndy));  
  
    // ndy update his work dates  
    ndyWorkDates.get(0).setDate(30);  
  
    // pjp check ndy's work dates  
    System.out.println("ndy work days : " + dayOfWorkByEmployee.getWorkDates(ndy));  
}
```

```
ndy work days : [Sat Apr 26 00:00:00 KST 3924, Mon Apr 28 00:00:00 KST 3924, Fri Apr 25 00:00:00 KST 3924, Sun Apr 27 00:00:00 KST 3924, Tue Apr 29 00:00:00 KST 3924]
ndy work days : [Sat Apr 26 00:00:00 KST 3924, Mon Apr 28 00:00:00 KST 3924, Wed Apr 30 00:00:00 KST 3924, Sun Apr 27 00:00:00 KST 3924, Tue Apr 29 00:00:00 KST 3924]
```

이를 막기 위해 Class 에서는 근무일자 추가 시 전달받은 Date 객체를 바로 내부 Map 에 저장하는 것이 아니라 방어적 복사본을 활용해야 한다.

```java
void addWorkDate(String name, Date date) {  
    Date dateCopied = new Date(date.getTime());  
    if (map.containsKey(name)) {  
        map.get(name).add(dateCopied);  
    } else {  
        HashSet<Date> workDates = new HashSet<>();  
        workDates.add(dateCopied);  
        map.put(name, workDates);  
    }  
}
```

수정 시 ndy 의 근무일자 수정 편법은 통하지 않는다.

## 방어적 복사본을 사용하는것이 불가능하거나 제한된다면 문서화를 해라 

혹은 클라이언트를 믿거나 최대한 불변 객체, 컬렉션을 사용해라.