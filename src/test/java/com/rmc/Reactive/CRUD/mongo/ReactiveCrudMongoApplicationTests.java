package com.rmc.Reactive.CRUD.mongo;

import com.rmc.Reactive.CRUD.mongo.controller.ProductController;
import com.rmc.Reactive.CRUD.mongo.dto.ProductDto;
import com.rmc.Reactive.CRUD.mongo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class ReactiveCrudMongoApplicationTests {
	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private ProductService productService;

	@Test
	public void addProductTest(){
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto(1090, "mobile", 4, 10000.0));
		when(productService.saveProduct(productDtoMono)).thenReturn(productDtoMono);
		webTestClient.post().uri("/products/save")
				.body(Mono.just(productDtoMono), ProductDto.class)
				.exchange()
				.expectStatus().isOk(); //200
	}
	@Test
	public void getAllProductsTest(){
		ProductDto productA = new ProductDto(34, "PX4", 6, 7648.0);
		ProductDto productB = new ProductDto(35, "PX5", 5, 5555);
		Flux<ProductDto> productDtoFlux = Flux.just(productA,productB);
		when(productService.getProducts()).thenReturn(productDtoFlux);
		Flux<ProductDto> responseBody = webTestClient.get().uri("/products")
				.exchange()
				.expectStatus().isOk() // 200
				.returnResult(ProductDto.class)
				.getResponseBody();
		StepVerifier.create(responseBody)
				.expectSubscription()
//				.expectNext(productA)
//				.expectNext(productB)
				.expectNextMatches(p -> p.getName().equals("PX4"))
				.expectNextMatches(p -> p.getName().equals("PX5"))
				.verifyComplete();
	}
	@Test
	public void getSingleProductTest(){
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto(3, "Iphone", 8, 7837));
		when(productService.getProduct(anyInt())).thenReturn(productDtoMono);
		Flux<ProductDto> responseBody = webTestClient.get().uri("/products/3")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();
		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNextMatches(p -> p.getName().equals("Iphone"))
				.verifyComplete();
	}
	@Test
public void updateProductTest(){
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto(9, "mobile", 345, 7894.5));
		when(productService.updateProduct(productDtoMono, 9)).thenReturn(productDtoMono);
		webTestClient.put().uri("/products/update/9")
				.body(Mono.just(productDtoMono), ProductDto.class)
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();
//		StepVerifier.create(responseBody)
//				.expectSubscription()
//				.expectNextMatches(p -> p.getName().equals("mobile"))
//				.verifyComplete();
	}

	@Test
	public void deleteProduct(){
		given(productService.deleteProduct(anyInt())).willReturn(Mono.empty());
		webTestClient.delete().uri("/products/delete/9")
				.exchange()
				.expectStatus().isOk();
	}
}
