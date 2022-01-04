package com.github.prgrms.orders;

import java.time.LocalDateTime;
import org.springframework.beans.BeanUtils;

public class OrderDto {

	private Long seq;

	private Long productId;

	private ReviewDto review;

	private OrderState state;

	private String requestMessage;

	private String rejectMessage;

	private LocalDateTime completedAt;

	private LocalDateTime rejectedAt;

	private LocalDateTime createdAt;

	public OrderDto(ProductOrder productOrder) {
		this(productOrder, null);
	}

	public OrderDto(ProductOrder productOrder, Review review) {
		BeanUtils.copyProperties(productOrder, this);
		if(review != null) {
			this.review = new ReviewDto(review);
		}
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public ReviewDto getReview() {
		return review;
	}

	public void setReview(ReviewDto review) {
		this.review = review;
	}

	public OrderState getState() {
		return state;
	}

	public void setState(OrderState state) {
		this.state = state;
	}

	public String getRequestMessage() {
		return requestMessage;
	}

	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}

	public String getRejectMessage() {
		return rejectMessage;
	}

	public void setRejectMessage(String rejectMessage) {
		this.rejectMessage = rejectMessage;
	}

	public LocalDateTime getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}

	public LocalDateTime getRejectedAt() {
		return rejectedAt;
	}

	public void setRejectedAt(LocalDateTime rejectedAt) {
		this.rejectedAt = rejectedAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
