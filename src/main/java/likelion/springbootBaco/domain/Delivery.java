package likelion.springbootBaco.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static likelion.springbootBaco.domain.Delivery.DeliveryStatus.ESTABLISHED;
import static lombok.AccessLevel.PROTECTED;

@Entity
/*
* Entity 어노테이션은 이 class가 JPA 객체로 사용될 것을 의미한다. 정확하게 얘기하면 RDS의 table 형태가 될 것이라는 의미인데 이것은
* Primary key가 있고 각각의 RDS마다의 Create 문을 만들 수 있는 즉 Create 문으로 mapping 할수 있는 class라는 것을 명시하는것이다.
* user가 이렇게 쓸꺼야 라고 얘기하는 것 이외에 spring boot에서 실제로 DDL 문으로 쓸 수 있어야 하기 때문에 @id가 없으면 빨간줄이 뜬다.
* 그래서 나는 빨간줄 뜨는걸 병적으로 싫어해서 @Id를 먼저 쓰고 @Entitiy를 쓴다.
*
* */
@NoArgsConstructor(access = PROTECTED)
/*
* NoArgsConstructor에 대한 설명은 Address class에서 설명했기 때문에 생략하고 access=PROTECTED라는건 저 생성자를 PROTECTED method로 설정한다는
* 뜻이다. protected는 같은 package 내에서 접근이 가능하다는 뜻인데 public으로 하지 않아야 하는 이유는 다른 package 즉 다른 프로젝트에서의 남용을 막겠다는
* 이유라고 볼 수 있는데 private를 해서 다른 class에서의 접근을 막으면 되지 않냐고 생각 할 수 있는데 join을 할때  JPA proxy 객체를 만든다. 이때 기존 class를 상속해서
* 만드는데 private 는 상속받는 class에서 접근 할 수 없기 때문에 protect를 쓴다. 이게 다 lazy Fetch 떄문이다. 하하
* 근데 실질적으로 access=PROTECTED를 쓰는 이유는 NoArgsConstructor는 생성자에 어떤 arg도 안넣어도 되는데 이때 setter로 모든걸 다 넣기에는 인간이 실수할 수도있는데
* 문제는 java는 앞에서 말했다시피 null을 병적으로 싫어한다. 처음부터 그럴일을 만들지 않으면 되지 않냐고 해서 밑에 구현되어있는 생성자로 만들어서 예외사항을 두는 방식을 고안하였다.
* */
@Getter
/*
* 모든 member 변수들에 따른 get함수를 생성해주는 어노테이션
* */
public class Delivery {
    @Id @GeneratedValue
    /*
    * Id는 primary key를 의미하고 당연히 겹치는 값이 없다.
    * GeneratedValue는 1부터 시작해서 개체가 늘어날때마다 1씩 증가시켜서 값을 넣는다는 의미이다. 그러면 당연히 값이 겹치지 않는다
    * 근데 개체가 많아질때의 overflow를 생각해야하고 중간 값이 삭제 되어서 예를 들어 value=100 인 값이 없어졌다고 하더라도 Id를 100으로 잡지는 않는다.
    * */
    private Long id;

    @OneToOne(mappedBy = "delivery")
    /*
    * Delivery class와 1대 1 mapping이 된다. 즉 delivery 하나에 1개의 order가 mapping 된다는 것이다. 원래 1대 1 전체참여의 경우에는
    * 같은 entity로 두어서 join을 최소화 시키는게 좋다고 했는데 order과 delivery의 관계가 전체참여 관계가 아니기 때문에 (주문을 한다고 하더라도
    * 포장이 있기 떄문에) 즉 null 값이 있을 수 있기 때문에 다른 개체로 넣은 듯하다. table에 null이 많아지면 공간을 효율적으로 사용하지 못하기 때문이다.
    * */
    private Order order;

    @Enumerated(STRING)
    /*
    * Enum 안쓴지 한 몇년된것 같은데 오랜만에 보니까 반가운것 같다. string 형태의 enum을 쓴다는 어노테이션이다. DeliveryStatus 라는 type 객체인데
    * 열거형 객체이다. 한정된 값을 갖는데 그 값이 단순히 true, false만으로 표현이 안될 때 혹은 true와 false 그 이상의 의미를 지닐때 사용한다.
    *
    * */
    private DeliveryStatus deliveryStatus;
    private String city;
    private String state;
    private String street;
    private String zipcode;
    /*
    * 수업때 바코님이 얘기했지만 도대체 얘네들은 왜 Address로 안묶었나 싶다. 이러면 Address class를 왜 만들었는지...
    *
    * */
    public static Delivery createDelivery(Order order, String city, String state, String street, String zipcode) {
        Delivery delivery = new Delivery();
        delivery.order = order;
        delivery.deliveryStatus = ESTABLISHED;
        delivery.city = city;
        delivery.state = state;
        delivery.street = street;
        delivery.zipcode = zipcode;
        return delivery;
    }
    /*
    * 앞에서 만든 생성자이다. Order city State Street, zipcode를 받아서 넣어준다. 자동 주입이 아니기 때문에 새롭게 instance를 new로 생성해 하나씩
    * 넣어주는 모습을 볼 수 있다.
    *
    * */
    public enum DeliveryStatus {
        ESTABLISHED, PROGRESS, DONE
    }
    /*
    * 앞에서 말한 enum class이다. 주문을 받은거 진행하는거 끝난거로 구분하는 것을 볼 수 있다. 이게 위에서 string으로 어노테이션을 해놔서 실제 table에서 보면
    * string 형태로 저장되어있다. 이런 표시가 없으면 ESTABLISHED부터 차례로 0,1,2로 뜨는데  enum은 기본적으로 "상수"의 열거이고 각각의 원소는 상수이다.
    * 그래서 대문자로 저렇게 쓰는것이다.(1학년때 이거 몰라서 c언어 과제 망쳤다.)
    *
    * */
}
