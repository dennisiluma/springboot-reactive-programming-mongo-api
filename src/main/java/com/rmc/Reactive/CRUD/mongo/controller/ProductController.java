package com.rmc.Reactive.CRUD.mongo.controller;

import com.rmc.Reactive.CRUD.mongo.dto.ProductDto;
import com.rmc.Reactive.CRUD.mongo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Flux<ProductDto> getAllProducts(){
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public Mono<ProductDto> getSingleProduct(@PathVariable Integer id){
        System.out.println("HEHEHEHEEHEH");
        return productService.getProduct(id);
    }

    @GetMapping("/productRange")
    public Flux<ProductDto> getProductRange(@RequestParam("min") double min, @RequestParam("max") double max){
        return productService.getProductInRange(min, max);
    }
    @PostMapping("/save")
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDtoMono){
        return productService.saveProduct(productDtoMono);
    }
    @PutMapping("/update/{id}")
    public Mono<ProductDto> updateProduct(@RequestBody Mono<ProductDto> productDtoMono, @PathVariable Integer id){
        System.out.println("Put");
        return productService.updateProduct(productDtoMono, id);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteProduct(@PathVariable Integer id){
        System.out.println("Delete Mapping Called");
        return productService.deleteProduct(id);
    }
}
