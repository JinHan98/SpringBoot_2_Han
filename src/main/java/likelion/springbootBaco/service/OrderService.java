package likelion.springbootBaco.service;

import likelion.springbootBaco.domain.Order;

import java.util.List;

public interface OrderService {
    public Long createOrder(Long memberId, Long itemId, int count);

    public void cancelOrder(Long orderId);

    public List<Order> findOrderList();
}
