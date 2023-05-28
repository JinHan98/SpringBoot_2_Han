package likelion.springbootBaco.service;

import likelion.springbootBaco.domain.*;
import likelion.springbootBaco.repository.ItemRepository;
import likelion.springbootBaco.repository.MemberRepository;
import likelion.springbootBaco.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public Long createOrder(Long memberId, Long itemId, int count) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalMember.isPresent() && optionalItem.isPresent()) {
            Member findMember = optionalMember.get();
            Item findItem = optionalItem.get();
            OrderItem orderItem = OrderItem.createOrderItem(findItem, findItem.getPrice(), count);
            Order order = Order.createOrder(findMember, orderItem);
            orderRepository.save(order);
            return order.getId();
        } else {
            throw new IllegalStateException("잘못된 요청입니다.");
        }
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order findOrder = optionalOrder.get();
            findOrder.cancel();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findOrderList() {
        return orderRepository.findAll();
    }
}
