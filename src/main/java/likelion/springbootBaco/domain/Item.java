package likelion.springbootBaco.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity // Delivery 참조
@Getter // Delivery 참조
@Setter // getter와 마찬가지로 set함수를 만들어 주는 어노테이션 앞에서 말했다시피 안쓰는게 좋다.
@NoArgsConstructor //Delivery 참조
public class Item {
    @Id @GeneratedValue //Delivery 참조
    private Long id;

    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItem = new ArrayList<>();
    /*
    * OrderItem 과 1:N 관계이다. item은 여러개의 orderItem에 담긴다. 즉 상대가 Many 내가 one이다. 사실 ERD를 스키마로 바꿀 때
    * 다 쪽에 attribute를 foreign key로 넣는데 JPA의 이런 구조는 정말 특이하다. 객체지향 프로그래밍에 억지로 관계성 table을 끼워 넣기 때문에 만들어
    * 지는 기형적인 구조이다.
    * 여기에서 이렇게 처리를 하는 이유는 join을 할 때마다 DB에 접속하면 실행시간이 느려지기 떄문에 한번에 받아서 mapping을 하겠다는 건데
    * 사실상 insert 나 update가 item 쪽에서 일어나면 이 list를 수정해야하는데 실무에서는 그렇게 좋은 방법이 아니라고 들었다.
    * 하지만 단순히 검색만 한다면 성능은 훌륭하다. 많이 생각해봐야하는 문제이다.
    *
    *
    * */

    private String brand;
    private String name;
    private Integer price;
    private Integer stock;
    /*
    * stock를 int로 설장하지 않고 Integer로 설정한 이유는 JPA 객체로 repository를 만들고 table에 저장될 때 객체 형식이여야지 type을 맞출수 있어서
    * 이다.
    * */
    /**
     * 비즈니스 로직
     */
    @Comment("재고 추가")
    public void addStock(int quantity) {
        this.stock += quantity;
    }
    /*
    * 수량만큼 stock을 증가시키는 함수
    * */
    @Comment("재고 감소")
    public void removeStock(int stockQuantity) {
        int restStock = this.stock - stockQuantity;
        if (restStock < 0) {
            throw new IllegalStateException("need more stock");
        }
        this.stock = restStock;
    }
    /*
     * 수량만큼 stock을 감소시키는 함수
     * */
}
