package com.junggotown.service;

import com.junggotown.domain.Orders;
import com.junggotown.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;

    public Orders saveOrder(UUID paymentId, Long productId, String userId) {
        return ordersRepository.save(Orders.getOrders(paymentId, productId, userId));
    }
}
