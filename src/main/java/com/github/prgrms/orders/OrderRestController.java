package com.github.prgrms.orders;

import static com.github.prgrms.utils.ApiUtils.*;

import com.github.prgrms.configures.web.SimplePageRequest;
import com.github.prgrms.reviews.Review;
import com.github.prgrms.reviews.ReviewService;
import com.github.prgrms.security.JwtAuthentication;
import java.util.LinkedList;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/orders")
public class OrderRestController {
  // DONE findAll, findById, accept, reject, shipping, complete 메소드 구현이 필요합니다.

	private final OrderService orderService;
	private final ReviewService reviewService;

	public OrderRestController(
		OrderService orderService,
		ReviewService reviewService
	) {
		this.orderService = orderService;
		this.reviewService = reviewService;
	}

	@GetMapping("/{orderId}")
	public ApiResult<OrderDto> findById(
		@AuthenticationPrincipal JwtAuthentication authentication,
		@PathVariable("orderId") long orderId) {
		orderService.authorizeOrder(authentication.id, orderId);
		ProductOrder productOrder = orderService.findById(orderId);
		Review review = reviewService.findOptionalReviewById(productOrder.getReview())
									 .orElse(null);
		return success(new OrderDto(productOrder, review));
	}

	@GetMapping
	public ApiResult<List<OrderDto>> findAll(
		@AuthenticationPrincipal JwtAuthentication authentication,
		SimplePageRequest request) {
		List<OrderDto> result = new LinkedList<>();
		List<ProductOrder> productOrders = orderService.findAll(
			authentication.id,
			request.getOffset(),
			request.getSize());
		productOrders.forEach(order -> {
			Review review = reviewService.findOptionalReviewById(order.getReview())
										 .orElse(null);
			result.add(new OrderDto(order, review));
		});
		return success(result);
	}

	@PatchMapping("/{id}/accept")
	public ApiResult<Boolean> accept(
		@AuthenticationPrincipal JwtAuthentication jwtAuthentication,
		@PathVariable("id") long orderId) {
		orderService.authorizeOrder(jwtAuthentication.id, orderId);
		return success(orderService.tryAcceptOrder(orderId));
	}

	@PatchMapping("/{id}/reject")
	public ApiResult<Boolean> reject(
		@AuthenticationPrincipal JwtAuthentication authentication,
		@PathVariable("id") long orderId,
		@RequestBody RejectRequest request) {
		orderService.authorizeOrder(authentication.id, orderId);
		return success(orderService.tryRejectOrder(orderId, request.getMessage()));
	}

	@PatchMapping("/{id}/shipping")
	public ApiResult<Boolean> shipping(
		@AuthenticationPrincipal JwtAuthentication authentication,
		@PathVariable("id") long orderId
	) {
		orderService.authorizeOrder(authentication.id, orderId);
		return success(orderService.tryShipOrder(orderId));
	}

	@PatchMapping("/{id}/complete")
	public ApiResult<Boolean> complete(
		@AuthenticationPrincipal JwtAuthentication authentication,
		@PathVariable("id") long orderId
	) {
		orderService.authorizeOrder(authentication.id, orderId);
		return success(orderService.tryCompleteOrder(orderId));
	}

}