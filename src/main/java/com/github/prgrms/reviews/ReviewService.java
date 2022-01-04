package com.github.prgrms.reviews;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import com.github.prgrms.errors.NotFoundException;
import com.github.prgrms.orders.OrderRepository;
import com.github.prgrms.orders.ProductOrder;
import com.github.prgrms.products.ProductService;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final OrderRepository orderRepository;
	private final ProductService productService;

	public ReviewService(
		ReviewRepository reviewRepository,
		OrderRepository orderRepository,
		ProductService productService
	) {
		this.reviewRepository = reviewRepository;
		this.orderRepository = orderRepository;
		this.productService = productService;
	}

	public Review createReview(Long userId, Long orderId, String content) {
		checkArgument(isNotEmpty(userId), "user id cannot be null.");
		checkArgument(isNotEmpty(orderId), "order id cannot be null.");
		checkArgument(isNotEmpty(content), "review content cannot be null.");

		ProductOrder productOrder = orderRepository.findById(orderId)
												   .orElseThrow(() -> new NotFoundException("order not found."));
		checkArgument(isEmpty(productOrder.getReview()), "order can be reviewed only once.");
		Review review = reviewRepository.save(
			new Review(
				userId,
				productOrder.getProductId(),
				content
			));
		productOrder.setReview(review);
		productService.incrementReviewCount(productOrder.getProductId());
		orderRepository.update(productOrder);
		return review;
	}

	@Transactional(readOnly = true)
	public Optional<Review> findOptionalReviewById(Long reviewId) {
		if (reviewId == null) {
			return Optional.empty();
		}

		return reviewRepository.findById(reviewId);
	}

}
