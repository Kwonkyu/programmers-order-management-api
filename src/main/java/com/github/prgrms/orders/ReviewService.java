package com.github.prgrms.orders;

import static com.google.common.base.Preconditions.*;

import com.github.prgrms.errors.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final OrderRepository orderRepository;

	public ReviewService(
		ReviewRepository reviewRepository,
		OrderRepository orderRepository
	) {
		this.reviewRepository = reviewRepository;
		this.orderRepository = orderRepository;
	}

	public Review createReview(Long userId, Long orderId, String content) {
		checkNotNull(userId, "user id cannot be null.");
		checkNotNull(orderId, "order id cannot be null.");
		checkNotNull(content, "review content cannot be null.");

		Order order = orderRepository.findById(orderId)
									 .orElseThrow(() -> new NotFoundException("order not found."));
		if(order.isReviewed()) {
			throw new IllegalArgumentException(String.format(
				"Could not write review for order %d because have already written",
				orderId));
		}

		if(!order.isReviewable()) {
			throw new IllegalArgumentException(String.format(
				"Could not write review for order %d because state(REQUESTED) is not allowed",
				orderId
			));
		}

		Review review = new Review(userId, order.getProductId(), content);
		return reviewRepository.save(review);
	}

}
