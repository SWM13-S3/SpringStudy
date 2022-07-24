# Java의 접근제어자
public / private / default / protected<br>
**변수, 메소드, 클래스**(편의상 객체라 하겠다... 이걸 뭐라고 해야하지? 아래에도 계속 객체라고 나올 텐데 어떤 의미에서 사용한 것인지 잘 구분해주세요) 선언 시 붙일 수 있고 해당 객체에 대한 접근을 제한하는데 사용된다.

1. public
* 어느 곳에서든 호출/접근이 가능함
    * 접근에 제한을 둘 필요가 없는 객체에 사용
* @Transactional 어노테이션은 public 메소드에 달았을 때만 제 기능으로 동작함
```java
@Transactional // 트랜잭션 정책 적용 안됨
private String method1() { // IntelliJ 에서는 컴파일 에러 발생
	...
}

@Transactional // 트랜잭션 정책 적용
public String method2() {
	...
}
```


2. protected
* 동일 패키지 또는 하위 클래스에서만 접근이 가능하다.
* 보통은 public으로 해결 가능한 메소드이다. 그렇다면 protected는 언제 왜 사용하는가?
    * 하위 클래스에서 Override하여 바꿀 가능성을 열어두는 것
    ```java
    //protected example
    public class Man {
        protected void attack(){
            System.out.println("때리기");
        }
    }

    public class Archer extends Man {
        protected void attack(){
            System.out.println("활쏘기");
        }
    }
    ```
    * 위의 예제에서 abstract vs protected 차이점은 무엇일까?<br>
        * abstract는 선언 시에 구현을 하지 않고 상속받는 클래스에서 재정의를 필수적으로 요구함
        * protected는 재정의를 요구하지 않음
* 다들 김영한 강의를 들으므로... Jpashop에서의 사용 예제(JPA 활용 1편 엔티티 클래스 개발2 14:52~)
```java
package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {
    }

    public Address(String city, String street, String zipcode){
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
```
city, street, zipcode라는 세 개의 정보를 Address라는 값 타입으로 만든 것이다.<br>
값 타입은 변경 불가능하도록 설계되어야만 해서 setter를 제공하지 않는다.<br>
기본 생성자는 꼭 있어야해서 만들었는데 public은 너무 개방적이고 JPA 스펙 상 protected를 권장한다는 의미로 이해했다<br>
어차피 여기에서의 protected는 큰 의미도 없으므로(다른 누군가 Address 클래스를 상속 받을 일도 없음) 그냥 그른갑다~하고 넘어가자<br>

3. default
* 접근제어자를 생략하면 기본으로 설정된다.
* 같은 패키지 내의 클래스에서 접근이 가능하다.

4. private
* 실질적으로 접근 권한을 '제한'하고 싶을때 주로, 대부분, 통상적으로 private을 쓴다.
* 해당 클래스 내부에서만 사용할 수 있기 때문에 해당 로직(주로 값 변경)이 어딘가에서 무분별하게 호출되는 것을 막기 위해 사용한다.


5. 접근 권한 총 정리

|종류|Class|Package|Lower Class|All|
|---|---|---|---|---|
|public|O|O|O|O|
|protected|O|O|O|X|
|default|O|O|X|X|
|private|O|X|X|X|

# static / final

1. final

final은 **단 한번** 할당될 수 있는 객체에 사용된다. 구현한 코드의 변경이 일어나지 않기를 원할 때 사용하자.
* final로 선언된 변수를 수정하려는 코드가 있으면 에러가 발생한다.
    * 아래의 경우는 에러가 발생할까?
    ```java
    public static void main(){
        final Item item = new Item();

        Item.setName("MacBook Pro"); // 이 때 에러가 발생할까?
    }
    ```
    정답 : X; Item 객체가 immutable(불변)한 것은 아니다. 객체의 속성은 변경할 수 있다.
* final 메소드의 경우 오버라이드가 불가능하다.
* final 클래스의 경우 상속이 불가능하다.

김영한 스프링 강의에서 final이 사용됐던 예제를 보자.
```java
@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;
    ...
}
```
Repository 계층은 EntityManager의 의존성 주입을 받아야 한다.(받아야 할 것이다)<br>
의존성 주입 방법은 생성자, setter, field 등이 있으나 가장 무난한 것은 생성자를 이용한 주입이다.<br>
그런데 RequiredArgsConstructor 어노테이션은 클래스 내에 final로 선언된 필드의 생성자를 자동으로 생성해준다(Lombok의 힘이다!)<br>
따라서 RequiredArgsConstructor 어노테이션을 넣어주고 final로 선언하면 편하게 의존성 주입을 할 수 있다.


2. static

'객체' 와 '클래스'의 차이점을 안다고 가정하겠다!<br>
static으로 선언된 변수와 메소드는 객체가 아니라 클래스에 묶인다.<br>
따라서!<br>
* 객체를 여러 개 생성해도 클래스에 할당된 메모리 영역에 있으므로 모든 객체가 같은 변수값, 메소드를 **공유**할 수 있다.
* 인스턴스화하지 않아도 static 메소드를 호출할 수 있다.
* Garbage Collector의 관리 영역 밖에 있으므로 프로그램 종료 시까지 메모리에 할당된 상태를 유지한다. 그래서 static 변수, 메소드가 너무 많으면 시스템에 악영향이 있을 수 있다!

static을 사용함으로써 메모리 상의 이득과 손해를 보는 경우를 정리하고 넘어가자.

3. 싱글톤 패턴

static에 대해 알았다면 싱글톤 패턴이 무엇인지 알 수 있다.<br>
싱글톤 : 단 하나의 객체만을 생성하도록 강제하는 패턴
```java
class Singleton {
    private static Singleton one;
    private Singleton() {
    }

    public static Singleton getInstance() {
        if(one==null) {
            one = new Singleton();
        }
        return one;
    }
}

public class Sample {
    public static void main(String[] args) {
        Singleton singleton1 = Singleton.getInstance();
        Singleton singleton2 = Singleton.getInstance();
        System.out.println(singleton1 == singleton2);  // true 출력
    }
}
```
* 장점/사용하는 경우
    * 인스턴스를 **하나만** 만들도록 강제해야 하는 상황
    * 두 번째 이용(singleton2)의 경우 이미 만들어진 인스턴스를 가져오기만 하므로 로딩 시간이 빠름
* 단점
    * 멀티쓰레드 환경에서 동기화를 제대로 안하면 인스턴스가 두 개가 동시에 생성되어 싱글톤 패턴이 깨짐
    * 싱글톤이 너무 잡다하게 많은 일, 데이터를 처리하게 되면 객체 지향 설계 원리를 벗어나게 됨

써야 할 때만(그걸 아는게 어렵지만..) 잘 쓰자!


# 뇌절을 쳐보자
```
private 생성자를 가진 클래스를 스프링 빈으로 등록할 수 있을까?
```
먼저 이 질문에 대한 답은 '리플렉션'이라는 개념을 알아야 이해할 수 있을 것 같은데 이는 추후로 넘기도록 하겠다.<br>
내가 공부한 것은 접근제어자이므로 여기에 집중하면, 생성자를 private으로 만들면 new를 이용한 생성이 불가능하고 대신 정적 팩토리 메소드(static factory method)를 이용하여 생성해야 한다.
## 정적 팩토리 메소드(static factory method)
```java
// LocalTime.class
...
public static LocalTime of(int hour, int minute) {
  ChronoField.HOUR_OF_DAY.checkValidValue((long)hour);
  if (minute == 0) {
    return HOURS[hour];
  } else {
    ChronoField.MINUTE_OF_HOUR.checkValidValue((long)minute);
    return new LocalTime(hour, minute, 0, 0);
  }
}
...

// hour, minutes을 인자로 받아서 9시 30분을 의미하는 LocalTime 객체를 반환한다.
LocalTime openTime = LocalTime.of(9, 30);
```
* 생성자를 통해 객체를 생성하는 것이 아닌, 어떤 메소드를 통해 객체를 생성하는 것을 정적 팩토리 메소드라고 한다.
* 이를 사용하는 것을 **팩토리 패턴**이라고 한다.
* 생성자와 정적 팩토리 메소드 모두 객체를 생성한다는 동일한 기능을 수행하는 것 같은데, 어떤 차이점이 있는 것인가?
    * 장점
    1. 이름을 가질 수 있다 : 객체의 생성 목적을 담아낼 수 있다
    2. 매번 새로운 객체를 생성하지 않아도 된다
    3. 하위 자료형 객체를 반환할 수 있다(2번과 동일한 맥락)
    4. 객체 생성을 캡슐화할 수 있다.

    -> 좀 더 가독성있고 객체지향적 프로그래밍을 할 수 있게 도와주며, 해당 도메인에서 '객체 생성'이 중요한 역할일 경우 사용하면 좋다.
    
    * 단점
    1. 팩토리 메소드만 존재하는 객체는 상속이 불가능하다



## 다음에 공부하고 싶은 내용
* Reflection
* Garbage Collector


## 자료 출처
* https://hyerm-coding.tistory.com/m/95
* https://velog.io/@kmdngmn/Spring-Transactional-privatepublic-%EC%A0%91%EA%B7%BC%EC%A0%9C%EC%96%B4%EC%9E%90
* https://wikidocs.net/232
* https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=2feelus&logNo=220576845725
* https://groups.google.com/g/ksug/c/eg3vxl4D6Bs?pli=1
* https://tecoble.techcourse.co.kr/post/2020-05-26-static-factory-method/
* https://advenoh.tistory.com/13
* https://wikidocs.net/228
* https://coding-factory.tistory.com/524