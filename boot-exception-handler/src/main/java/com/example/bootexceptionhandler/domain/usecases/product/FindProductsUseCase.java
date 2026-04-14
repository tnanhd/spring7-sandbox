package com.example.bootexceptionhandler.domain.usecases.product;

import com.example.bootexceptionhandler.domain.models.product.Product;

import java.util.List;

public class FindProductsUseCase {

  public List<Product> execute() {
    return List.of(
        new Product("Product 1", 10.0),
        new Product("Product 2", 20.0),
        new Product("Product 3", 30.0));
  }
}
