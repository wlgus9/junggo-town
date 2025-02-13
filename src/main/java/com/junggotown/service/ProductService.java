package com.junggotown.service;

import com.junggotown.domain.Product;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.product.ProductDto;
import com.junggotown.dto.product.ResponseProductDto;
import com.junggotown.global.common.ProductStatus;
import com.junggotown.global.common.ResponseMessage;
import com.junggotown.global.exception.CustomException;
import com.junggotown.global.jwt.JwtProvider;
import com.junggotown.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public ApiResponseDto<ResponseProductDto> create(ProductDto productDto, HttpServletRequest request) {
        Product product = getEntity(productDto, request);
        Long id = productRepository.save(product).getId();

        return ApiResponseDto.response(ResponseMessage.PRODUCT_CREATE_SUCCESS, ResponseProductDto.getCreateDto(id));
    }

    // 가상계좌 발급에 필요한 상품 정보 조회
    public Product getProduct(Long productId, String userId) {
        return productRepository.findByIdAndUserId(productId, userId)
                .orElseThrow(() -> new CustomException(ResponseMessage.PRODUCT_IS_NOT_YOURS));
    }

    public ApiResponseDto<ResponseProductDto> searchByProductId(ProductDto productDto, HttpServletRequest request) {
        Product product = getEntity(productDto, request);

        return productRepository.findByIdAndUserId(product.getId(), product.getUserId())
                .map(products -> ApiResponseDto.response(
                        ResponseMessage.PRODUCT_SEARCH_SUCCESS
                        , ResponseProductDto.getSearchDto(products))
                )
                .orElseThrow(() -> new CustomException(ResponseMessage.PRODUCT_IS_NOT_YOURS));
    }

    public ApiResponseDto<List<ResponseProductDto>> searchByUserId(ProductDto productDto) {
        Product product = Product.getProductFromDto(productDto);

        return productRepository.findByUserId(product.getUserId())
                .filter(products -> !products.isEmpty())
                .map(products -> {
                    List<ResponseProductDto> returnDto = products.stream()
                            .map(ResponseProductDto::getSearchDto)
                            .collect(Collectors.toList());
                    return ApiResponseDto.response(ResponseMessage.PRODUCT_SEARCH_SUCCESS, returnDto);
                })
                .orElseGet(() -> ApiResponseDto.response(ResponseMessage.PRODUCT_IS_EMPTY));
    }

    @Transactional
    public ApiResponseDto<ResponseProductDto> update(ProductDto productDto, HttpServletRequest request) {
        Product product = getEntity(productDto, request);

        if(isMyProduct(product)) {
            return productRepository.findById(productRepository.save(product).getId())
                            .map(products -> ApiResponseDto.response(
                                    ResponseMessage.PRODUCT_UPDATE_SUCCESS
                                    , ResponseProductDto.getSearchDto(products))
                            )
                            .orElseThrow(() -> new CustomException(ResponseMessage.PRODUCT_UPDATE_FAIL));
        } else {
            throw new CustomException(ResponseMessage.PRODUCT_IS_NOT_YOURS);
        }
    }

    @Transactional
    public ApiResponseDto<ResponseProductDto> saleStop(ProductDto productDto, HttpServletRequest request) {
        Product product = getEntity(productDto, request);

        if(isMyProduct(product)) {
            product.changeStatus(ProductStatus.SALE_STOP);
            return ApiResponseDto.response(ResponseMessage.PRODUCT_SALESTOP_SUCCESS);
        } else {
            throw new CustomException(ResponseMessage.PRODUCT_IS_NOT_YOURS);
        }
    }

    @Transactional
    public ApiResponseDto<ResponseProductDto> soldOut(ProductDto productDto, HttpServletRequest request) {
        Product product = getEntity(productDto, request);

        if(isMyProduct(product)) {
            product.changeStatus(ProductStatus.SOLD_OUT);
            return ApiResponseDto.response(ResponseMessage.PRODUCT_SOLDOUT_SUCCESS);
        } else {
            throw new CustomException(ResponseMessage.PRODUCT_IS_NOT_YOURS);
        }
    }

    @Transactional
    public ApiResponseDto<ResponseProductDto> delete(ProductDto productDto, HttpServletRequest request) {
        Product product = getEntity(productDto, request);

        if(isMyProduct(product)) {
            productRepository.deleteById(product.getId());
            return ApiResponseDto.response(ResponseMessage.PRODUCT_DELETE_SUCCESS);
        } else {
            throw new CustomException(ResponseMessage.PRODUCT_IS_NOT_YOURS);
        }
    }

    public Product getEntity(ProductDto productDto, HttpServletRequest request) {
        return Product.getProductFromDto(productDto, jwtProvider.getUserId(request));
    }

    public boolean isMyProduct(Product product) {
        return productRepository.findByIdAndUserId(product.getId(), product.getUserId()).isPresent();
    }
}
