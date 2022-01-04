package com.github.prgrms.products;

import com.github.prgrms.errors.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Transactional(readOnly = true)
  public Optional<Product> findById(Long productId) {
    checkNotNull(productId, "productId must be provided");

    return productRepository.findById(productId);
  }

  @Transactional(readOnly = true)
  public List<Product> findAll() {
    return productRepository.findAll();
  }

  public Product incrementReviewCount(long productId) {
    Product product = productRepository.findById(productId)
                                       .orElseThrow(
                                           () -> new NotFoundException("product not found."));
    product.incrementReviewCount();
    productRepository.update(product);
    return product;
  }

}