package com.example.bootexceptionhandler.features.product;

import com.example.bootexceptionhandler.domain.exception.AmountLimitExceededException;
import com.example.bootexceptionhandler.domain.exception.DomainException;
import com.example.bootexceptionhandler.domain.usecases.product.FindProductsUseCase;
import com.example.bootexceptionhandler.infra.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductController.class)
@ContextConfiguration(classes = {ProductController.class, GlobalExceptionHandler.class})
class ProductControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private FindProductsUseCase findProductsUseCase;

  @Test
  void testFindProducts_ShouldReturnSuccess() throws Exception {
    mockMvc
        .perform(get("/api/products"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray());
  }

  @Test
  void testFindProducts_ShouldReturnGeneralBadRequest() throws Exception {
    when(findProductsUseCase.execute()).thenThrow(new DomainException("General error"));

    mockMvc
        .perform(get("/api/products"))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.title").value("Domain Rule Violation"))
        .andExpect(jsonPath("$.detail").value("General error"));
  }

  @Test
  void testFindProducts_ShouldReturnUnprocessableContent() throws Exception {
    when(findProductsUseCase.execute()).thenThrow(new AmountLimitExceededException("Amount limit exceeded"));

    mockMvc
        .perform(get("/api/products"))
        .andExpect(status().isUnprocessableContent())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.title").value("Domain Rule Violation"))
        .andExpect(jsonPath("$.detail").value("Amount limit exceeded"));
  }
}
