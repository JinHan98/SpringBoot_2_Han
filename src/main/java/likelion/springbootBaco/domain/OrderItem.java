package likelion.springbootBaco.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity // Delivery 참조
@NoArgsConstructor(access = PROTECTED) // Delivery 참조
@Getter // Delivery 참조
public class OrderItem {
    @Id
    @GeneratedValue // Delivery 참조
    private Long id;

    @ManyToOne(fetch = LAZY) // Order 참조
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = LAZY) // Order 참조
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer price; // Item 참조
    private Integer count; // Item 참조

    /**
     * 스태틱 팩토리 메서드
     */
    public static OrderItem createOrderItem(Item item, int orderPrice, int orderCount) { //Order 참조
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.price = orderPrice;
        orderItem.count = orderCount;
        // 연관관계 편의 메서드
        item.removeStock(orderCount);
        return orderItem;
    }

    public void setOrder(Order order) { //Order 참조 연관 관계 편의 매서드
        this.order = order;
        order.getOrderItemList().add(this);
    }

    public void setItem(Item item) {  //Order 참조 연관 관계 편의 매서드
        this.item = item;
        item.getOrderItem().add(this);
    }

    /**
     * 비즈니스 로직
     */
    public int getTotalPrice() {
        return this.getPrice() * this.getCount();
    } //전체 price를 반환하는 함수 수량*가격

    public void cancel() {
        this.getItem().addStock(count);
    } // Order 에서 주문이 취소 되었을 때 각각의 수량을 원래대로 되돌려 놓는 함수
}
