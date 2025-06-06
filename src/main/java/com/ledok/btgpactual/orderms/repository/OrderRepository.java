package com.ledok.btgpactual.orderms.repository;


import com.ledok.btgpactual.orderms.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderEntity, Long> {

    Page<OrderEntity> findAllByCustumerId(Long custumerId, PageRequest pageRequest);
}
