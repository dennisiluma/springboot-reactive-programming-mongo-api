package com.rmc.Reactive.CRUD.mongo.service;

import com.rmc.Reactive.CRUD.mongo.dto.ProductDto;
import com.rmc.Reactive.CRUD.mongo.entity.Product;
import com.rmc.Reactive.CRUD.mongo.repository.ProductRepository;
import com.rmc.Reactive.CRUD.mongo.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Flux<ProductDto> getProducts(){
        return productRepository.findAll().map(AppUtils::entityToDto);
    }
    public Mono<ProductDto> getProduct(int id){
        return  productRepository.findById(id).map(AppUtils::entityToDto);
    }
    public Flux<ProductDto> getProductInRange(double min, double max){
        return productRepository.findByPriceBetween(Range.closed(min, max));
    }
    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDto){
        return productDto.map(AppUtils::dtoToEntity)
                .flatMap(productRepository::insert)
                .map(AppUtils::entityToDto);
    }
    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono, Integer id){
        return productRepository.findById(id)
                .flatMap(p-> productDtoMono.map(AppUtils::dtoToEntity)
                .doOnNext(e->e.setId(id)))
                .flatMap(productRepository::save)
                .map(AppUtils::entityToDto);
    }
    public Mono<Void> deleteProduct(Integer id){
        return productRepository.deleteById(id);
    }
}
