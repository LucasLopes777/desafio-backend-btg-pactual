package com.ledok.btgpactual.orderms.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@Document("tb_oerders")
public class OrderEntity {

    @MongoId
    private Long oderId;

    @Indexed(name = "custumer_id_index")
    private Long custumerId;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal total;

    private List<OrderItem> items;

}
