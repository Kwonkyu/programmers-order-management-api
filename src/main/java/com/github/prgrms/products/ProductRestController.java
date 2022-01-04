package com.github.prgrms.products;

import static com.github.prgrms.utils.ApiUtils.*;
import static java.util.stream.Collectors.*;

import com.github.prgrms.errors.NotFoundException;
import com.github.prgrms.utils.ApiUtils;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/products")
public class ProductRestController {

	private final ProductService productService;

	public ProductRestController(ProductService productService) {
		this.productService = productService;
	}

	// [DONE] `요건 1` 정의에 맞게 응답 타입 수정이 필요합니다.
	@GetMapping(path = "{id}")
	public ApiResult<ProductDto> findById(@PathVariable Long id) {
		return ApiUtils.success(
			productService.findById(id)
						  .map(ProductDto::new)
						  .orElseThrow(
							  () -> new NotFoundException("Could not found product for " + id)));
	}

	// [DONE] `요건 1` 정의에 맞게 응답 타입 수정이 필요합니다.
	@GetMapping
	public ApiResult<List<ProductDto>> findAll() {
		return ApiUtils.success(
			productService.findAll()
						  .stream()
						  .map(ProductDto::new)
						  .collect(toList()));
	}

}