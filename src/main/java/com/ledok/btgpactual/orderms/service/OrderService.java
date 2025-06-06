package com.ledok.btgpactual.orderms.service;

import com.ledok.btgpactual.orderms.controller.dto.OrderResponse;
import com.ledok.btgpactual.orderms.entity.OrderEntity;
import com.ledok.btgpactual.orderms.entity.OrderItem;
import com.ledok.btgpactual.orderms.listener.dto.OrderCreatedEvent;
import com.ledok.btgpactual.orderms.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MongoTemplate mongoTemplate;

    public void save(OrderCreatedEvent event) {

        var entity = OrderEntity.builder()
                        .oderId(event.codigoPedido())
                        .custumerId(event.codigoCliente())
                        .items(getOrderItems(event))
                        .total(getTotal(event))
                        .build();

        orderRepository.save(entity);
    }

    public Page<OrderResponse> findAllByCustumerId(Long custumerId, PageRequest pageRequest) {
        var orders = orderRepository.findAllByCustumerId(custumerId, pageRequest);

        return orders.map(OrderResponse::fromEntity);
    }

    public BigDecimal findTotalOnOrdersByCustumerId(Long custumerId) {
        var aggregations = newAggregation(
                match(Criteria.where("custumerId").is(custumerId)),
                group().sum("total").as("total")
        );

        var response = mongoTemplate.aggregate(aggregations, "tb_oerders", Document.class);

        return new BigDecimal(response.getUniqueMappedResult().get("total").toString());
    }

    private BigDecimal getTotal(OrderCreatedEvent event) {
        return event.itens()
                .stream()
                .map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private static List<OrderItem> getOrderItems(OrderCreatedEvent event) {
        return event.itens()
                .stream()
                    .map(i ->
                        OrderItem.builder()
                                .product(i.produto())
                                .quantity(i.quantidade())
                                .price(i.preco())
                                .build()).toList();
    }
}
