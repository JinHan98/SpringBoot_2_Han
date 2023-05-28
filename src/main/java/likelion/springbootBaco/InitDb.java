package likelion.springbootBaco;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import likelion.springbootBaco.domain.Address;
import likelion.springbootBaco.domain.Item;
import likelion.springbootBaco.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {
            Address address1 = new Address("서울시", "용산구", "길", "11111");
            Address address2 = new Address("갓갓갓", "갓갓갓", "갓", "11111");
            Member member1 = Member.createMember("황제철", address1);
            Member member2 = Member.createMember("김주안", address2);
            em.persist(member1);
            em.persist(member2);

            Item item1 = new Item();
            item1.setBrand("무신사");
            item1.setName("스탠다드");
            item1.setPrice(100);
            item1.setStock(100);
            Item item2 = new Item();
            item2.setBrand("나이키");
            item2.setName("조던");
            item2.setPrice(300);
            item2.setStock(300);
            em.persist(item1);
            em.persist(item2);

        }
    }

}
