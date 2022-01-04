package com.github.prgrms.orders;

import static com.github.prgrms.utils.ApiUtils.*;

import com.github.prgrms.security.JwtAuthentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/orders")
public class ReviewRestController {
	// DONE review 메소드 구현이 필요합니다.
	private final ReviewService reviewService;

	public ReviewRestController(
		ReviewService reviewService
	) {
		this.reviewService = reviewService;
	}

	@PostMapping("/{id}/review")
	public ApiResult<ReviewDto> review(
		@AuthenticationPrincipal JwtAuthentication authentication,
		@PathVariable("id") long orderId,
		@RequestBody ReviewRequest request
	) {
		return success(
			new ReviewDto(
				reviewService.createReview(
					authentication.id,
					orderId,
					request.getContent()
				)));
	}

}