package com.example.bootexceptionhandler.infra.di;

import com.example.bootexceptionhandler.domain.usecases.product.FindProductsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductModule {

  @Bean
  FindProductsUseCase findProductsUseCase() {
    return new FindProductsUseCase();
  }
}
