package com.ledok.btgpactual.orderms.controller.dto;

import com.ledok.btgpactual.orderms.entity.OrderEntity;

import java.math.BigDecimal;

public record OrderResponse (Long orderId,
                             Long costumerId,
                             BigDecimal total) {

    public static OrderResponse fromEntity(OrderEntity entity) {
        return new OrderResponse(entity.getOderId(), entity.getCustumerId(), entity.getTotal());
    }
}
