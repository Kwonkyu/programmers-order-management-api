package com.github.prgrms.orders;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

	Optional<ProductOrder> findById(Long orderId);

	List<ProductOrder> findAll(long userId, long offset, int size);

	ProductOrder update(ProductOrder productOrder);

}
