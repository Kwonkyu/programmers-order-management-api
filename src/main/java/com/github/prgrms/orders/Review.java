package com.github.prgrms.orders;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.time.LocalDateTime;

public class Review {

	private Long seq;

	private final Long userId;

	private final Long productId;

	private String content;

	private final LocalDateTime createdAt;

	public Review(Long userId, Long productId, String content) {
		this(null, userId, productId, content, null);
	}

	public Review(
		Long seq,
		Long userId,
		Long productId,
		String content,
		LocalDateTime createdAt
	) {
		checkArgument(isNotEmpty(userId), "user id cannot be null.");
		checkArgument(isNotEmpty(productId), "product id cannot be null.");
		checkArgument(isNotEmpty(content), "review content cannot be null or empty.");
		checkArgument(
			content.length() >= 1 && content.length() < 1000,
			"review content length cannot exceed 1000 or less than 1."
		);

		this.seq = seq;
		this.userId = userId;
		this.productId = productId;
		this.content = content;
		this.createdAt = defaultIfNull(createdAt, LocalDateTime.now());
	}

	public void setContent(String content) {
		checkArgument(
			isEmpty(content) || content.length() < 1 || content.length() >= 1000,
			"review content length must be less than 1000, over 0 characters."
		);

		this.content = content;
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public Long getUserId() {
		return userId;
	}

	public Long getProductId() {
		return productId;
	}

	public String getContent() {
		return content;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public static class Builder {

		private Long seq;
		private Long userId;
		private Long productId;
		private String content;
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

		public Builder content(String content) {
			this.content = content;
			return this;
		}

		public Builder createdAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public Review build() {
			return new Review(
				seq,
				userId,
				productId,
				content,
				createdAt
			);
		}
	}
}
