package likelion.springbootBaco.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
/*
* Embeddable은 써도 되고 쓰지 않아도 되는 어노테이션으로써 밑의 Address class가 다른 domain class에 embed 될 수 있다는 것을 표현한 어노테이션이다.
* Embeddable 어노테이션을 사용하여 각각의 Entity 에 embed하면
*  객체지향 프로그래밍에서 코드의 재사용성을 높이고 entity간의 중복요소를 없애 더욱 더 객체 지향적으로 표현할 수 있게 해주는 효과가 있다.
* */
@Data
/*
* Data 어노테이션은 lombok에 정의 되어있는 어노테이션으로써 private 객체만 있는 class에 필요한 Getter 와 Setter 뿐만 아니라 String으로 변환해주는
* Tostring, 자동으로 생성자를 주입해주는 RequiredArgsConstructor 등이 있다.
* 사실상 종합선물세트인데 너무 많은것이 구현되어있어서 굳이라는 생각이 들긴 하는 어노테이션이다.
* */
@AllArgsConstructor
@NoArgsConstructor
/*
 * 위의 두 어노테이션은 RequiredArgsConstructor 랑 같이 묶을 수 있는데 NoArgsConstructor 는 Arg 즉 argument(parameter) 값이 없는 채로
 * 생성자를 만든다는 뜻이고 RequiredArgsConstructor 는 선택적으로 argument를 넣을 수 있는 생성자를 만든다는 뜻이고
 * AllArgsConstructor 는 모든 argument가 전부 들어가있는 생성자를 만든다는 뜻이다.
 * 사실 이거 3개만 있으면 setter는 필요가 없다. 특히 java는 null pointer 문제를 해결하기 위해 재대입 하는 코딩을 싫어하기 때문에 생성자로 딱 값이 정해지고
 * 바뀌지 않는 코딩이 안정적이라고 하며 권장하긴 한다. 취향차이라고 보기에는 코드 안정성 문제니까 setter보단 constructor를 쓰는게 좋아보인다.
 *
 * */
public class Address {
    private String city;
    private String state;
    private String street;
    private String zipcode;
    /*
    * 각각 다 주소를 나타내는 attribute 들이다. 만약 이것을 바탕으로 JPA instance를 만든다면 Address 라는 table 이름에 varchar(255)->default
    * attribute가 4개가 생긴다. 각각의 이름은 변수 이름과 같다. 하지만 repository class를 생성하지 않았기 때문에 table이 그대로 생성될리는 없다.
    * 어차피 embedded용이기 때문이다.
    * */
}
