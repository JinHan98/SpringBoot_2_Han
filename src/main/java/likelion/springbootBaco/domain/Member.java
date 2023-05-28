package likelion.springbootBaco.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity //Delivery 참조
@Getter //Delivery 참조
@NoArgsConstructor(access = PROTECTED) //Delivery 참조
public class Member {
    @Id @GeneratedValue //Delivery 참조
    private Long id;

    private String name; //String 객체 사람이름을 가지고 있다.

    @OneToMany(mappedBy = "member")
    private List<Order> orderList = new ArrayList<>(); //이것 역시 oneTomany 관계 Item에서 설명해 놓음

    @Embedded // Address class를 Embedded 해왔다. Embeddable은 어노테이션이 없어도 되지만 @Embedded는 필요하다. IoC containter에서 받아 와야함
    private Address address;

    public static Member createMember(String name, Address address) {
        Member member = new Member();
        member.name = name;
        member.address = address;
        return member;
    }
    /*
    * Delivery 참조 name과 address를 꼭 넣어서 생성자를 만들어야한다. 아니면 protected여야 한다면서 빨간줄
    * */
}
