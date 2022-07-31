# GoF 디자인 패턴


GoF는 Gang of Four의 줄임말로, `에리히 감마(Erich Gamma)`, `리처드 헬름(Richard Helm)`, `랄프 존슨(Ralph Johnson)`, `존 블리시데스(John Vlissides)` 4인방을 의미한다.

이들은 소프트웨어 설계에 있어 공통된 문제들에 대한 표준적인 해법과 작명법을 제안한 책을 저술하였다.

이들이 소프트웨어 설계의 노하우를 축적하여 재이용하기 좋은 형태로 특정 규약을 묶어서 정리한 것을 `GoF디자인 패턴`이라 한다.

# 구분 기준

목적에 따라 구분하면 `생성`, `구조`, `행동` 세 가지를 기준으로 나눌 수 있다.

`생성`: 객체를 생성하는 데 관련된 패턴으로 객체가 생성되는 과정의 유연성을 높이고 코드의 유지를 쉽게 함

`구조`: 프로그램 구조에 관련된 패턴으로 프로그램 내의 자료구조나 인터페이스 등 프로그램의 구조를 설계하는 데 활용할 수 있는 패턴들

`행동`: 반복적으로 사용되는 객체들의 상호작용을 패턴화 해놓은 것들

# GoF디자인 패턴 종류

| 생성(Creational) | 구조(Structural) | 행동(Behavioral) |
| --- | --- | --- |
| Abstract Factory(추상 팩토리) | Adapter(어댑터) | Chain of Responsibility(책임 연쇄) |
| Builder(빌더) | Bridge(브릿지) | Command(커맨드) |
| Factory Method(팩토리 메서드) | Composite(컴포지트) | Interpreter(인터프리터) |
| Prototype(프로토타입) | Facade(퍼싸드) | Iterator(반복자) |
| Singleton(싱글톤) | Flyweight(플라이웨이트) | Mediator(중재자) |
|  | Proxy(프록시) | Memento(메멘토) |
|  |  | Observer(옵저버) |
|  |  | State(상태) |
|  |  | Strategy(전략) |
|  |  | Template Method(템플릿 메소드) |
|  |  | Visitor(방문자) |

## 생성 패턴(Creational Pattern)

생성 패턴에는 `Factory Method 패턴`, `Abstract Factory 패턴`이 존재한다.

**이 두가지를 이해하기 위해서는 `Simple Factory`에 대해 알아야 한다.**

### Simple Factory란?

객체를 생성해내는 공장을 따로 두는 것을 의미한다.

즉, `객체 생성 부분을 전담하는 클래스`가 따로 있는 것이다.

Simple Factory를 설명하기 위해 피자로 예를 드는 경우가 많았다.

우리도, 피자를 예를 들어 생각해보자.

피자는 피자 가게에서 직접 생성하지 않고 피자 공장을 거쳐 생성하도록 하는 것이다.

![Untitled](GoF%20%E1%84%83%E1%85%B5%E1%84%8C%E1%85%A1%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%91%E1%85%A2%E1%84%90%E1%85%A5%E1%86%AB%20918d1962e3d940b294c33b189145f5c5/Untitled.png)

위 사진은 Star UML로 만든 Pizza 관련 Class Diagram이다.

→와 같은 화살표는 연관이 있다, 정도로 생각하면 된다. (화살표가 향하는 방향에 해당하는 클래스를 사용한다 정도)

화살표가 더 두꺼운 것 (ex CheesePizza→Pizza)의 경우, 화살표가 향하는 쪽이 부모, 화살표가 나오는 쪽이 자식으로, 상속받았다는 것을 의미한다.

상속을 받았다는 것은, 자바의 중요한 성질중 하나인 `다형성`을 활용할 수 있다는 것이다!

위 사진 중에 SimplePizzaFactory가 바로 객체의 생성을 담당하는 클래스로, Simple Factory를 적용한 예시이다.

```java
public class PizzaStore {
	SimplePizzaFactory factory;
    

    public PizzaStore (SimplePizzaFactory factory) {
		this.factory = factory;
    }
    
    public Pizza orderPizza (String type) {
    	Pizza pizza;
        
        pizza = factory.createPizza(type); // 팩토리를 써서 pizza 객체를 만든다.
        
**        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();
        
        return pizza;
    }
}
```

```java
public class SimplePizzaFactory
{
	public Pizza createPizza(String type)
    {
    	Pizza pizza = null;
        
        if( type.equals("cheese") )
        	pizza = new CheesePizza();
        else if( type.equals("pepperoni") )
        	pizza = new PepperoniPizza();
        else if( type.equals("clam") )
        	pizza = new ClamPizza();
        
        return pizza;
    }
}
```

위 두 코드가 Simple Factory를 사용한 예시이다.

앞서 말한 Factory 클래스가 Pizza 객체의 생성을 담당하고 있는 것을 볼 수 있다.

장점

이를 활용하면 객체를 생성하는 작업을 한 곳에 모아, 객체 생성 부분에 수정 사항이 있을 때 Factory 클래스만 변경해 주면 된다는 장점을 갖고 있다. 즉, 수정 용이, 유지 보수 비용 절감의 효과가 있다.

### 팩토리 메서드 패턴

조건에 따른 객체 생성을 팩토리 클래스로 위임하여, 팩토리 클래스에서 객체를 생성하는 패턴을 의미한다.

위의 Simple Factory를 예로 들면, createPizza가 PizzaFactory에 선언되어있는 것을 확인할 수 있었다.

여기서 createPizza를 추상 메서드로 만든다고 해보자.

![Untitled](GoF%20%E1%84%83%E1%85%B5%E1%84%8C%E1%85%A1%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%91%E1%85%A2%E1%84%90%E1%85%A5%E1%86%AB%20918d1962e3d940b294c33b189145f5c5/Untitled%201.png)

그렇다면 위 다이어그램과 같은 형태가 될 것이다.

여기서 Factory가 사용되어 `객체 생성`을 담당하는 클래스가 따로 존재한다는 것을 알고 넘어가자.

구현된 코드를 확인해보면

```java
public abstract class PizzaStore {
	public Pizza orderPizza(String type) {
    	Pizza pizza;
        
        pizza = createPizza(type);  // 팩토리 객체가 아닌 Pizza store에 있는 createPizza를 호출함
        
        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();
        
        return pizza;
	}
    
    abstract Pizza createPizza(String type); // 팩토리 메소드
}
```

```java
// NYPizzaStore
public class ChicagoPizzaStore extends PizzaStore {
	Pizza createPizza(String item) {
    	if( item.equals("cheese") )
        	return new ChicagoStyleCheesePizza();
        else if( item.equals("pepperoni") )
        	return new ChicagoStylePepperoniPizza();
        else if( item.equals("clam") )
        	return new ChicagoStyleClamPizza();
        else
        	return null;
     }
}
```

여기서 주목해서 봐야 할 점은 PizzaStore 클래스에서 createPizza()라는 메서드가 추상 메서드로 되어있다는 점이다.

```java
public static void main(){
	PizzaStore pizzaStore = new ChicagoPizzaStore();

	pizzaStore.orderPizza("cheese"); // => ChicagoStyleCheesePizza
	
}
```

이를 사용하는 메인 함수를 생각해보자.

여기서 pizza를 조건에 따라 직접 new 하여 생성하는 것이 아니라, pizzaStore에 따라 다른 Cheese 피자를 생성해준다는 것을 확인할 수 있다.

객체 생성을 직접 하지 않고 서브 클래스에 위임하여 의존성을 제거한다는 점이 중요하다.

여기서 만약, ChicagoPizzaStore이 아닌 NYPizzaStore에서 주문하기를 원하면, 

`new ChicagoPizzaStore()` 을 `new NYPizzaStore()`으로만 바꾸면 된다.

orderPizza에서의 파라미터인 `cheese`를 변경할 필요가 없어진다.

즉 FactoryMethod Pattern 사용의 장점은

- 클래스 객체를 생성하는 부분, 사용하는 부분 분리하여 낮은 결합도
- 다른 클래스를 추가하거나 클래스 객체의 구성을 변경시키더라도 객체를 생성하는 부분은 건드릴 필요가 없음

### 추상 팩토리 패턴(Abstract Factory Pattern)

구체적인 클래스에 의존하지 않고, 서로 연관되거나 의존적인 객체들의 조합을 만드는 인터페이스를 제공(추상화)하는 패턴

관련성 있는 여러 종류의 객체를 일관된 방식으로 생성하는 경우 사용된다.

![Untitled](GoF%20%E1%84%83%E1%85%B5%E1%84%8C%E1%85%A1%E1%84%8B%E1%85%B5%E1%86%AB%20%E1%84%91%E1%85%A2%E1%84%90%E1%85%A5%E1%86%AB%20918d1962e3d940b294c33b189145f5c5/Untitled%202.png)

이제, 피자에 들어가는 구성요소를 생각해보자.

도우가 있을 것이고, 소스가 있을 것이다.

시카고 피자는 도우가 두껍고, NY피자는 도우가 얇다고 생각해보자.

```java
public interface PizzaIngredientFactory {
		public Dough createDough();
    public Sauce createSauce();
    public Cheese createCheese();
    public Pepperoni createPepperoni();
}
```

```java
public class NYPizzaIngredientFactory implements PizzaIngredientFactory {
	public Dough createDough() {
    	return new ThinCrustDough();
    }
    public Sauce createSauce() {
    	return new MarinaraSauce();
    }
    public Cheese createCheese() {
    	return new ReggianoCheese();
    }
    public Pepperoni createPepperoni() {
    	return new SlicePepperoni();
    }
    public Clams createClam() {
    	return new FreshClams();
    }
}
```

위 처럼 재료를 생성하는 부분을 담당하는 Factory를 인터페이스로 만들고, chicago와 NY의 피자 공장이 이를 상속받는다. 여기서, 메서드를 오버라이딩 하며 어떤 재료를 쓸지를 정해준다.

```java
public class CheesePizza extends Pizza {
	PizzaIngredientFactory ingredientFactory;

    // 각 피자 클래스에서는 생성자를 통해서 팩토리를 전달 받는다.
    // 이 팩토리는 인스턴스 변수에 저장한다.
    public CheesePizza(PizzaIngredientFactory ingredientFactory) {
    	this.ingredientFactory = ingredientFactory;
    }

    void prepare() {
    	System.out.println("Preparing " + name);
        dough = ingredientFactory.createDough();
        sauce = ingredientFactory.createSauce();
        cheese = ingredientFactory.createCheese();
    	// 재료가 필요할 때마다 팩토리에 있는 메소드를 호출해서 만들어 온다.
    }
}
```

Cheese피자를 생성할 때 NYPizzaStore은 당연하게 NYPizzaIngredientFactor을 사용할 것이다. 이를 인자 값으로 하여 CheesePizza를 생성하면, NYPizzaIngredientFactor에서 NY 피자에 맞는 재료들로 CheesePizza를 만들어 준다.

즉, cheese피자를 생성하고자 할 때, 상위 클래스는 도우가 얇은지 두꺼운지를 신경 쓸 필요가 없이 단순히 cheese피자를 생성한다! 고만 일러주면 하위 클래스가 어떤 도우를 사용할지 정하는 것이다.

```java
public class NYPizzaStore extends PizzaStore {
	protected Pizza createPizza(String item) {
    	Pizza pizza = null;
        PizzaIngredientFactory ingredientFactory = new NYPizzaIngredientFactory();
    	// 뉴욕 피자 가게에는 뉴욕 피자 원재료 공장을 전달해줘야 한다.
        // 뉴욕풍 피자를 만들기 위한 재료는 이 공장에서 공급된다.

        if( item.equals("cheese") ) {
        	pizza = new CheesePizza(ingredientFactory);
            // 피자 재료를 위해 쓸 팩토리를 각 피자 객체에 전달해준다.
            pizza.setName("New York Style Cheese Pizza");
        } else if( item.equals("clam") ) {
        	pizza = new ClamPizza(ingredientFactory);
            pizza.setName("New York Style Clam Pizza");
        } else if( item.equals("pepperoni") ) {
        	pizza = new PepperoniPizza(ingredientFactory);
            pizza.setName("New York Style Pepperoni Pizza");
        }
        // 각 형식의 피자마다 새로운 Pizza인스턴스를 만들고 원재료를 공급 받는데 필요한 팩토리를 지정해정해준다.
        return pizza;
    }
}
```

추상 팩토리의 장점은 구상 클래스에 의존하지 않고 서로 연관된 객체들로 구성할 수 있다는 장점

Pizza 공장에 따라 어떤 재료 객체를 사용할 지 정하게 됨

→ 메인에서는, 어떤 피자 가게가 두꺼운 도우를 쓰는지, 얇은 도우를 쓰는지 신경쓸 필요가 없다!

→ 하위 클래스가 잘 만들어 줌!

```java
public static void main(){
	PizzaStore pizzaStore = new NYPizzaStore();
	cheesePizza = pizzaStore.createPizza("cheese");
	
}
```

결국 알아야 할 것은

인터페이스로 캡슐화하여 안의 내용을 보이지 않게 하고, (알 필요가 없게 하고)

그럼으로써 결합도를 낮춘다는 장점이 있다는 것이다.

사실 다형성을 사용한 예로, 객체를 생성하는 부분과 사용하는 부분을 나누었다는 점,

이 때문에 변경 시 용이하다는 점만 기억하면 될 것 같다.

~~팩토리에 대해 공부하는데, 듣는 사람의 머리 속에는 피자밖에 남지 않을 것 같다 ^^~~

~~마치 클래스 공부하는데 머리 속에 붕어빵밖에 남지 않은 것처럼 …~~

여기서 알 수 없는 한가지

다형성으로 해결할 수 있는 문제고

이를 잘 사용하고 있는데,

어떤 상황에서 사용해야 이 패턴이 유용하게 쓰일까?

### **적용 가능성**

다음과 같은 경우 추상 팩토리 패턴을 사용하십시오.

- 시스템은 제품이 생성, 구성 및 표현되는 방식과 독립적이어야 합니다.
- 시스템은 여러 제품군 중 하나로 구성되어야 합니다.
- 관련 제품 개체 제품군은 함께 사용하도록 설계되었으며 이 제약 조건을 적용해야 합니다.
- 제품의 클래스 라이브러리를 제공하고 구현이 아닌 인터페이스만 공개하려는 경우
- 종속성의 수명은 개념적으로 소비자의 수명보다 짧습니다.
- 특정 종속성을 구성하려면 런타임 값이 필요합니다.
- 런타임에 제품군에서 호출할 제품을 결정하려고 합니다.
- 종속성을 해결하기 전에 런타임에만 알려진 하나 이상의 매개변수를 제공해야 합니다.
- 제품 간의 일관성이 필요할 때
- 프로그램에 새 제품이나 제품군을 추가할 때 기존 코드를 변경하고 싶지 않습니다.

라고 나와있다.

번역체지만 앞에서 언급한 내용과 비슷하다.

객체 생성하는 부분과 사용하는 부분을 분리하고, 같은 기능을 하는 클래스끼리 묶어 인터페이스를 생성해야 한다,

어떤 객체를 선택할 지는 런타임 시간에 알 수 있다.
등 .

s