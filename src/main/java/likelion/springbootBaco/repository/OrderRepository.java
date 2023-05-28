package likelion.springbootBaco.repository;

import likelion.springbootBaco.domain.Order;
import likelion.springbootBaco.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
