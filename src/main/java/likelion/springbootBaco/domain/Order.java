package likelion.springbootBaco.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity // Delivery 참조
@Table(name = "orders") // 이거 안하면 에러 <- 원래는 Order이름이 그대로 table이 되는데 예약어여서 안되는걸로 알고 있음
@Getter // Delivery 참조
@NoArgsConstructor(access = PROTECTED) //Delivery 참조
public class Order {
    @Id
    @GeneratedValue //Delivery 참조
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    /*
    * 드디어 나온 ManyToOne, OneToMany랑 다르게 하나의 table와 매칭된다. 실제 table에서도 foreign key로써 Orders table에 들어간다.
    * 여기서 fetch =LAZY 가 있는데 EAGER과 반대되는 개념이다. 이를 이해하려면 proxy에 대해서 설명이 필요한데 말 그대로 가짜를 의미한다.
    * 어쨌든 Order는 member와 join할 수 있기 때문에 Order 객체를 생성할 때 member 객체도 생성을 하고 Order 객체가 참조를 하게되는데 member 자체에
    * 접근할 때는 mebmer의 proxy에 접근을 하고 member의 attribute에 접근 할때 그때서야 DB에서 member를 가져온다. 말그대로 lazy하다.
    * 반대로 eager를 사용하면 proxy가 없이 처음부터 member의 attribute를 전부 가져오게 된다.
    * 그렇게 되면 lazy의 경우에는 query를 처음에 Order를 검색할때 한번 하고 다시 member를 검색할 때 한번 더 날리니까 eager이 더 좋은게 아니냐는 생각을
    * 할 수 있는데 문제는 eager를 사용하면 객체를 생성할 때 모든 query를 전부 생성하는데 이때 Order를 생성하고 그에 따른 member를 lazy에서는 proxy를 써서
    * 있다라고 가정만 하면 되는데 eager에서는 SELECT id as member_id FROM Member,Orders WHERE Orders.member_id=Member.id 쿼리를 member_id
    * 에 해당하는 N개의 개체에 대한 query를 각각 날려 member_id를 받아오는데 이를 N+1 문제라고 한다. 사실 관계형 DB에서는 위의 query를 그냥 날리면 되는데
    * 객체지향언어에 끼워 맞추다보니 저 쿼리에서 Member.id에 해당하는 부분에 실제 값들을 하나씩 넣어서 쿼리를 실행한다. 역시 JPA는 어거지다.
    * 그리고 JoinColunm 이름을 따로 member_id로 둔 이유는 member의 id와 Orders 에서의 member의 id를 구분하기 위해서이다.
    * */
    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
    /*
    위랑 똑같은데 cascade ALL이 있다. 이것은 RDB 명령어 옵션인데 이게 PK, FK 관계니까 PK가 바뀌면 그에 맞게 FK가 바뀌어야한다.
    DB의 참조 무결성을 위반하기 때문이다. 즉 FK가 그에 따른 PK가 없으면 DB에서 검색자체가 아니기 때문에 DB는 이러한 무결성을 꼭 지켜야한다.
    그래서 cascade, restrict, set null 같은 옵션을 주는데 set null은 데이터 공간을 낭비하기 때문에 좋은 방법이 아니고 restrict는 사용자 입장에서
    너무 번거롭기 때문에 cascade를 많이 쓴다.
    */
    @OneToMany(mappedBy = "order", cascade = ALL)
    private List<OrderItem> orderItemList = new ArrayList<>(); //위와 동일

    private LocalDateTime orderDate; // 주문한 날짜이고 localDateTime이라는 객체는 시간형 객체인데 RDB로 mapping 될 때 datetime 형으로 바뀐다.

    @Enumerated(EnumType.STRING) //이거역시 EnumType이 string이다. 이는 Delivery에서 설명하였다.
    private OrderStatus orderStatus;

    // 연관관계 편의 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getOrderList().add(this);
    }
    /*
    * 연관관계 편의 메서드이다. 사실 RDB 자체를 쓰기 위해서는 필요가 없다. RDB에서는 join Column을 한쪽에만 써주기 때문이다.
    * 하지만 또 이걸 객체지향언어가 파괴한다. 객체지향에서는 다쪽에도 표시를 해야한다. 뭐 당연히 연관관계를 전부 표현하고 객체끼리의 호출을 하면
    * 객체지향적으로 잘 짠 코드인데 데이터 아키텍처들이 보면 펄쩍 뛸 노릇이다.
    * 말 그대로 member에도 아까 orderlist를 만들었으니까 order에 member를 넣었으니까 memeber쪽에도 order를 넣는것이다.
    * DB를 먼저 배운 입장에서는 좀 불편하다....
    * */

    public static Order createOrder(Member member, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.orderDate = LocalDateTime.now();
        order.orderStatus = OrderStatus.ORDERED;
        order.delivery = Delivery.createDelivery(order, member.getAddress().getCity(),
                member.getAddress().getState(),
                member.getAddress().getStreet(),
                member.getAddress().getZipcode());
        for (OrderItem orderItem : orderItems) {
            order.orderItemList.add(orderItem);
            orderItem.setOrder(order);
        }
        return order;
    }
    /*
    * 말 그대로 새로운 Order를 만드는 함수인데 static으로 선언되어있다. 이게 Bean 주입등을 통해서 이 객체를 instance화 해서 쓰는 class에서
    * 쓰거나 Bean 주입이 아니더라도 new와 생성자를 통해 객체를 생성해서 쓰면 static이 필요가 없는데
    * 이걸 쓰는 OrderServiceimpl을 보면 이 클래스를 초기화시키거나 객체로도 안받아오는것을 볼 수 있다.
    * 사실 필요가 없다가 정답인데 이럴때 쓸 수 있는게 run 되기전에 OS에서 미리 담아주는 static 함수를 쓰는거 밖에 없다.
    * 이런 method를 정적 팩토리 메서드라고 하는데 공장에서 뭔가를 찍어내는것처럼 찍어내는건데 사실상 생성자랑 하는 역할은 같다.
    * 그래도 정적 팩토리 메서드를 사용하는 이유는 Bean 주입이나 class가 생성될 때 값을 주입하는것이 어렵거나 그럴 필요가 없을 때 많이 사용한다.
    * 특히 OrderServiceImpl class를 보면 JPA 객체인 repository에 값을 넣기 위해서 Order 객체를 생성하는 과정에서 정적팩토리 메서드를 사용
    * 하는 것을 볼 수 있다.
    * 그리고 인자가 OrderItem... 형 객체인데 list를 원소만 빼서 parameter에 담아주는 것이다. 제발 c++에도 이런 기능이 있었으면 좋겠다.
    * 위에서 짠 함수들을 통해서 Order 객체에 member도 넣고 attribute의 값도 추가하는 모습을 볼 수 있다.
    * */

    public void cancel() {
        if (delivery.getDeliveryStatus() == Delivery.DeliveryStatus.DONE) {
            throw new IllegalStateException("배송 완료했다 양아치야");//ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ
        }
        this.orderStatus = OrderStatus.CANCELED;
        for (OrderItem orderItem : orderItemList) {
            orderItem.cancel();
        }
    }
    /*
    * 중간에 취소를 하면 취소해주는 method이다. 이미 끝났는데 취소해달라는 양아치는 예외처리를 했고 아직 처리가 안된건 취소처리를 하였다.
    * 해당하는 orderItem 객체의 모든 수량을 전부 원래대로 바꿔주는 method이다.
    * 이 method가 static이 아닌이유는 OrderServiceImpl에서 instance화 시켜서 사용하기 때문이다.
    * */

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItemList) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
    /*
    * Itemlist의 가격과 수량을 곱한걸 전부 더해서 totalPrice를 만드는 함수
    * */
}
