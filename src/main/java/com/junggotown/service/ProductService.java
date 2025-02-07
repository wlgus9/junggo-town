package com.junggotown.service;

import com.junggotown.domain.Product;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.product.ProductDto;
import com.junggotown.dto.product.ResponseProductDto;
import com.junggotown.global.exception.product.ProductException;
import com.junggotown.global.jwt.JwtProvider;
import com.junggotown.global.message.ProductStatus;
import com.junggotown.global.message.ResponseMessage;
import com.junggotown.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final JwtProvider jwtProvider;

    public ApiResponseDto<ResponseProductDto> create(ProductDto productDto, HttpServletRequest request) throws ProductException {
        Product product = Product.getProductFromDto(productDto, jwtProvider.getUserId(request));
        Long id = productRepository.save(product).getId();

        return ApiResponseDto.response(ResponseMessage.PRODUCT_CREATE_SUCCESS, ResponseProductDto.getCreateDto(id));
    }

    public ApiResponseDto<ResponseProductDto> searchByProductId(ProductDto productDto, HttpServletRequest request) throws ProductException {
        Product product = Product.getProductFromDto(productDto, jwtProvider.getUserId(request));

        Product result = productRepository.findByIdAndUserId(product.getId(), product.getUserId());

        if(result != null) {
            return ApiResponseDto.response(ResponseMessage.PRODUCT_SEARCH_SUCCESS, ResponseProductDto.getSearchDto(result));
        } else {
            return ApiResponseDto.response(ResponseMessage.PRODUCT_IS_NOT_YOURS);
        }
    }

    public ApiResponseDto<List<ResponseProductDto>> searchByUserId(ProductDto productDto) throws ProductException {
        Product product = Product.getProductFromDto(productDto);
        Optional<List<Product>> productList = productRepository.findByUserId(product.getUserId());

        return productList
                .filter(products -> !products.isEmpty())
                .map(products -> {
                    List<ResponseProductDto> returnDto = products.stream()
                            .map(ResponseProductDto::getSearchDto)
                            .collect(Collectors.toList());
                    return ApiResponseDto.response(ResponseMessage.PRODUCT_SEARCH_SUCCESS, returnDto);
                })
                .orElseGet(() -> ApiResponseDto.response(ResponseMessage.PRODUCT_IS_EMPTY));
    }

    public ApiResponseDto<ResponseProductDto> update(ProductDto productDto, HttpServletRequest request) throws ProductException {
        Product product = Product.getProductFromDto(productDto, jwtProvider.getUserId(request));

        Product isExists = productRepository.findByIdAndUserId(product.getId(), product.getUserId());

        if(isExists != null) {
            Optional<Product> result = productRepository.findById(productRepository.save(product).getId());
            return ApiResponseDto.response(ResponseMessage.PRODUCT_UPDATE_SUCCESS, ResponseProductDto.getSearchDto(result.get()));
        } else {
            return ApiResponseDto.response(ResponseMessage.PRODUCT_IS_NOT_YOURS);
        }
    }

    public ApiResponseDto<ResponseProductDto> saleStop(ProductDto productDto, HttpServletRequest request) {
        Product product = Product.getProductFromDto(productDto, jwtProvider.getUserId(request));

        Product isExists = productRepository.findByIdAndUserId(product.getId(), product.getUserId());

        if(isExists != null) {
            product.changeStatus(ProductStatus.SALE_STOP);
            return ApiResponseDto.response(ResponseMessage.PRODUCT_SALESTOP_SUCCESS);
        } else {
            return ApiResponseDto.response(ResponseMessage.PRODUCT_IS_NOT_YOURS);
        }
    }

    public ApiResponseDto<ResponseProductDto> soldOut(ProductDto productDto, HttpServletRequest request) {
        Product product = Product.getProductFromDto(productDto, jwtProvider.getUserId(request));

        Product isExists = productRepository.findByIdAndUserId(product.getId(), product.getUserId());

        if(isExists != null) {
            product.changeStatus(ProductStatus.SOLD_OUT);
            return ApiResponseDto.response(ResponseMessage.PRODUCT_SOLDOUT_SUCCESS);
        } else {
            return ApiResponseDto.response(ResponseMessage.PRODUCT_IS_NOT_YOURS);
        }
    }

    public ApiResponseDto<ResponseProductDto> delete(ProductDto productDto, HttpServletRequest request) throws ProductException {
        Product product = Product.getProductFromDto(productDto, jwtProvider.getUserId(request));

        Product isExists = productRepository.findByIdAndUserId(product.getId(), product.getUserId());

        if(isExists != null) {
            productRepository.deleteById(product.getId());
            return ApiResponseDto.response(ResponseMessage.PRODUCT_DELETE_SUCCESS);
        } else {
            return ApiResponseDto.response(ResponseMessage.PRODUCT_IS_NOT_YOURS);
        }
    }
}
