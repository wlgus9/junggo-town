package com.junggotown.orders;

import com.junggotown.domain.Orders;
import com.junggotown.service.OrdersService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class OrdersTest {
    @Autowired
    private OrdersService ordersService;

    private final Orders orders = Orders.getOrders(UUID.randomUUID(), 1L, "test");

    @Test
    @DisplayName("주문 정보 저장 성공")
    void saveOrderSuccess() {
        Optional<Orders> result = Optional.of(ordersService.saveOrder(UUID.randomUUID(), 1L, "test"));

        assertThat(result.isPresent()).isEqualTo(true);
    }
}
