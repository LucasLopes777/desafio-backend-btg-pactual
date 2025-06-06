package com.ledok.btgpactual.orderms.listener;

import com.ledok.btgpactual.orderms.listener.dto.OrderCreatedEvent;
import com.ledok.btgpactual.orderms.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.ledok.btgpactual.orderms.config.RabbitMqConfig.ORDER_CREATED_QUEUE;

@Component
@RequiredArgsConstructor
public class OrderCreatedListener {

    private final Logger logger = LoggerFactory.getLogger(OrderCreatedListener.class);
    private final OrderService orderService;

    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    public void listen(Message<OrderCreatedEvent> message) {
        logger.info("Message consumed: {}", message);

        orderService.save(message.getPayload());
    }
}
