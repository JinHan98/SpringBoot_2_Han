package likelion.springbootBaco.repository;

import likelion.springbootBaco.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
