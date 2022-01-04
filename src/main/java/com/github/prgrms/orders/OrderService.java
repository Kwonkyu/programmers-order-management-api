package com.github.prgrms.orders;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import com.github.prgrms.errors.NotFoundException;
import com.github.prgrms.errors.UnauthorizedException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;

	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Transactional(readOnly = true)
	public void authorizeOrder(long userId, long orderId) {
		if(!orderRepository.findById(orderId)
					   .orElseThrow(() -> new NotFoundException("order not found."))
					   .getUserId().equals(userId)) {
			throw new UnauthorizedException(String.format(
				"access to order %d unauthorized to user %d.",
				orderId, userId
			));
		}
	}

	@Transactional(readOnly = true)
	public List<ProductOrder> findAll(long userId, long offset, int size) {
		return orderRepository.findAll(userId, offset, size);
	}

	@Transactional(readOnly = true)
	public ProductOrder findById(Long orderId) {
		checkArgument(isNotEmpty(orderId), "order id cannot be null.");

		return orderRepository.findById(orderId)
							  .orElseThrow(() -> new NotFoundException("order not found."));
	}

	public boolean tryAcceptOrder(long orderId) {
		ProductOrder productOrder = orderRepository.findById(orderId)
												   .orElseThrow(() -> new NotFoundException("order not found."));
		try {
			productOrder.accept();
			orderRepository.update(productOrder);
			return true;
		} catch (IllegalStateException ex) {
			return false;
		}
	}

	public boolean tryRejectOrder(long orderId, String rejectMessage) {
		ProductOrder productOrder = orderRepository.findById(orderId)
												   .orElseThrow(() -> new NotFoundException("order not found."));
		try {
			productOrder.reject(rejectMessage);
			orderRepository.update(productOrder);
			return true;
		} catch (IllegalStateException ex) {
			return false;
		}
	}

	public boolean tryShipOrder(long orderId) {
		ProductOrder productOrder = orderRepository.findById(orderId)
												   .orElseThrow(() -> new NotFoundException("order not found."));
		try {
			productOrder.ship();
			orderRepository.update(productOrder);
			return true;
		} catch (IllegalStateException ex) {
			return false;
		}
	}

	public boolean tryCompleteOrder(long orderId) {
		ProductOrder productOrder = orderRepository.findById(orderId)
												   .orElseThrow(() -> new NotFoundException("order not found."));
		try {
			productOrder.complete();
			orderRepository.update(productOrder);
			return true;
		} catch (IllegalStateException ex) {
			return false;
		}
	}

}
