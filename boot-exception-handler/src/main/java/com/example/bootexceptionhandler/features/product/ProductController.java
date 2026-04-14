package com.example.bootexceptionhandler.features.product;

import com.example.bootexceptionhandler.domain.models.product.Product;
import com.example.bootexceptionhandler.domain.usecases.product.FindProductsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

  private final FindProductsUseCase findProductsUseCase;

  @GetMapping
  public List<Product> findProducts() {
    return findProductsUseCase.execute();
  }
}
