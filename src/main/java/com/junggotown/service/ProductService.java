package com.junggotown.service;

import com.junggotown.domain.Product;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.product.ProductDto;
import com.junggotown.dto.product.ResponseProductDto;
import com.junggotown.global.exception.product.ProductException;
import com.junggotown.global.jwt.JwtProvider;
import com.junggotown.global.commonEnum.ProductStatus;
import com.junggotown.global.commonEnum.ResponseMessage;
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
        Product product = getEntity(productDto, request);
        Long id = productRepository.save(product).getId();

        return ApiResponseDto.response(ResponseMessage.PRODUCT_CREATE_SUCCESS, ResponseProductDto.getCreateDto(id));
    }

    // 가상계좌 발급에 필요한 상품 정보 조회
    public Product getProductInfo(Long productId, HttpServletRequest request) throws ProductException {
        Product product = getEntity(ProductDto.getSearchDto(productId), request);

        Product result = productRepository.findByIdAndUserId(product.getId(), product.getUserId());

        return Optional.ofNullable(result)
                .orElseThrow(() -> new ProductException(ResponseMessage.PRODUCT_IS_NOT_YOURS.getMessage()));
    }

    public ApiResponseDto<ResponseProductDto> searchByProductId(ProductDto productDto, HttpServletRequest request) throws ProductException {
        Product product = getEntity(productDto, request);

        Product result = productRepository.findByIdAndUserId(product.getId(), product.getUserId());

        if(result != null) {
            return ApiResponseDto.response(ResponseMessage.PRODUCT_SEARCH_SUCCESS, ResponseProductDto.getSearchDto(result));
        } else {
            throw new ProductException(ResponseMessage.PRODUCT_IS_NOT_YOURS.getMessage());
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
        Product product = getEntity(productDto, request);

        if(productIsMine(product)) {
            Optional<Product> result = productRepository.findById(productRepository.save(product).getId());
            return ApiResponseDto.response(ResponseMessage.PRODUCT_UPDATE_SUCCESS, ResponseProductDto.getSearchDto(result.get()));
        } else {
            throw new ProductException(ResponseMessage.PRODUCT_IS_NOT_YOURS.getMessage());
        }
    }

    public ApiResponseDto<ResponseProductDto> saleStop(ProductDto productDto, HttpServletRequest request) {
        Product product = getEntity(productDto, request);

        if(productIsMine(product)) {
            product.changeStatus(ProductStatus.SALE_STOP);
            return ApiResponseDto.response(ResponseMessage.PRODUCT_SALESTOP_SUCCESS);
        } else {
            throw new ProductException(ResponseMessage.PRODUCT_IS_NOT_YOURS.getMessage());
        }
    }

    public ApiResponseDto<ResponseProductDto> soldOut(ProductDto productDto, HttpServletRequest request) {
        Product product = getEntity(productDto, request);

        if(productIsMine(product)) {
            product.changeStatus(ProductStatus.SOLD_OUT);
            return ApiResponseDto.response(ResponseMessage.PRODUCT_SOLDOUT_SUCCESS);
        } else {
            throw new ProductException(ResponseMessage.PRODUCT_IS_NOT_YOURS.getMessage());
        }
    }

    public ApiResponseDto<ResponseProductDto> delete(ProductDto productDto, HttpServletRequest request) throws ProductException {
        Product product = getEntity(productDto, request);

        if(productIsMine(product)) {
            productRepository.deleteById(product.getId());
            return ApiResponseDto.response(ResponseMessage.PRODUCT_DELETE_SUCCESS);
        } else {
            throw new ProductException(ResponseMessage.PRODUCT_IS_NOT_YOURS.getMessage());
        }
    }

    public Product getEntity(ProductDto productDto, HttpServletRequest request) {
        return Product.getProductFromDto(productDto, jwtProvider.getUserId(request));
    }

    public boolean productIsMine(Product product) {
        return productRepository.findByIdAndUserId(product.getId(), product.getUserId()) != null;
    }
}
