package com.junggotown.global.jwt;

import com.junggotown.global.common.ResponseMessage;
import com.junggotown.global.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("인터셉터 실행");

        String token = jwtProvider.resolveToken(request);

        log.info("token = {}", token);

        if(token == null) throw new CustomException(ResponseMessage.MISSING_TOKEN);

        if(!jwtProvider.validateToken(token)) throw new CustomException(ResponseMessage.INVALID_TOKEN);

        String userId = jwtProvider.getUserId(token);

        request.setAttribute("token", token);
        request.setAttribute("id", userId);

        return true;
    }
}
