package com.github.prgrms.orders;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import com.github.prgrms.reviews.Review;
import java.time.LocalDateTime;

public class ProductOrder {

	private final Long seq;

	private final Long userId;

	private final Long productId;

	private Long review;

	private OrderState state;

	private String requestMessage;

	private String rejectMessage;

	private LocalDateTime completedAt;

	private LocalDateTime rejectedAt;

	private final LocalDateTime createdAt;

	public ProductOrder(
		Long userId,
		Long productId,
		Long review,
		String requestMessage
	) {
		this(
			null,
			userId,
			productId,
			review,
			null,
			requestMessage,
			null,
			null,
			null,
			null
		);
	}

	public ProductOrder(
		Long seq,
		Long userId,
		Long productId,
		Long review,
		OrderState state,
		String requestMessage,
		String rejectMessage,
		LocalDateTime completedAt,
		LocalDateTime rejectedAt,
		LocalDateTime createdAt
	) {
		checkArgument(isNotEmpty(userId), "user id cannot be null.");
		checkArgument(isNotEmpty(productId), "product id cannot be null.");

		this.seq = seq;
		this.userId = userId;
		this.productId = productId;
		this.review = review;
		this.state = defaultIfNull(state, OrderState.REQUESTED);
		this.requestMessage = requestMessage;
		this.rejectMessage = rejectMessage;
		this.completedAt = completedAt;
		this.rejectedAt = rejectedAt;
		this.createdAt = defaultIfNull(createdAt, LocalDateTime.now());
	}

	public void setReview(Review review) {
		if (isReviewed()) {
			throw new IllegalArgumentException(String.format(
				"Could not write review for order %d because have already written",
				seq
			));
		}

		if (isNotReviewable()) {
			throw new IllegalArgumentException(String.format(
				"Could not write review for order %d because state(REQUESTED) is not allowed",
				seq
			));
		}

		this.review = review.getSeq();
	}

	public void setRequestMessage(String msg) {
		checkArgument(
			msg != null && msg.length() < 1000 && msg.length() > 0,
			"request message length cannot exceed 1000 or shorter than 1 characters."
		);
		this.requestMessage = msg;
	}

	public void setRejectMessage(String msg) {
		checkArgument(
			msg != null && msg.length() < 1000 && msg.length() > 0,
			"reject message length cannot exceed 1000 or shorter than 1 characters."
		);
		this.rejectMessage = msg;
	}

	public void accept() {
		checkState(
			this.state.equals(OrderState.REQUESTED),
			"cannot accept already completed or rejected order."
		);
		completedAt = LocalDateTime.now();
		state = OrderState.ACCEPTED;
	}

	public void reject(String rejectMessage) {
		checkArgument(isNotEmpty(rejectMessage), "reject message cannot be empty.");
		checkState(
			this.state.equals(OrderState.REQUESTED),
			"cannot refuse already completed or rejected order."
		);
		rejectedAt = LocalDateTime.now();
		this.rejectMessage = rejectMessage;
		state = OrderState.REJECTED;
	}

	public void ship() {
		checkState(
			this.state.equals(OrderState.ACCEPTED),
			"cannot ship not accepted order."
		);
		this.state = OrderState.SHIPPING;
	}

	public void complete() {
		checkState(
			this.state.equals(OrderState.SHIPPING),
			"cannot complete not shipped order."
		);
		this.state = OrderState.COMPLETED;
		this.completedAt = LocalDateTime.now();
	}

	public boolean isNotReviewable() {
		return !this.state.equals(OrderState.COMPLETED);
	}

	public boolean isReviewed() {
		return this.review != null;
	}

	public Long getSeq() {
		return seq;
	}

	public Long getUserId() {
		return userId;
	}

	public Long getProductId() {
		return productId;
	}

	public Long getReview() {
		return review;
	}

	public OrderState getState() {
		return state;
	}

	public String getRequestMessage() {
		return requestMessage;
	}

	public String getRejectMessage() {
		return rejectMessage;
	}

	public LocalDateTime getCompletedAt() {
		return completedAt;
	}

	public LocalDateTime getRejectedAt() {
		return rejectedAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public static class Builder {

		private Long seq;

		private Long userId;

		private Long productId;

		private Long review;

		private OrderState state;

		private String requestMessage;

		private String rejectMessage;

		private LocalDateTime completedAt;

		private LocalDateTime rejectedAt;

		private LocalDateTime createdAt;

		public Builder seq(Long seq) {
			this.seq = seq;
			return this;
		}

		public Builder userId(Long userId) {
			this.userId = userId;
			return this;
		}

		public Builder productId(Long productId) {
			this.productId = productId;
			return this;
		}

		public Builder review(Long review) {
			this.review = review;
			return this;
		}

		public Builder state(OrderState state) {
			this.state = state;
			return this;
		}

		public Builder requestMsg(String requestMsg) {
			this.requestMessage = requestMsg;
			return this;
		}

		public Builder rejectMsg(String rejectMsg) {
			this.rejectMessage = rejectMsg;
			return this;
		}

		public Builder completedAt(LocalDateTime completedAt) {
			this.completedAt = completedAt;
			return this;
		}

		public Builder rejectedAt(LocalDateTime rejectedAt) {
			this.rejectedAt = rejectedAt;
			return this;
		}

		public Builder createdAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public ProductOrder build() {
			return new ProductOrder(
				seq,
				userId,
				productId,
				review,
				state,
				requestMessage,
				rejectMessage,
				completedAt,
				rejectedAt,
				createdAt
			);
		}
	}
}
