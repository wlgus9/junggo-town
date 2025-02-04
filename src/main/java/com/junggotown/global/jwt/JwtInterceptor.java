package com.junggotown.global.jwt;

import com.junggotown.global.exception.token.InvalidTokenException;
import com.junggotown.global.exception.token.MissingTokenException;
import com.junggotown.global.message.ResponseMessage;
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

        if(token == null) throw new MissingTokenException(ResponseMessage.MISSING_TOKEN.getMessage());

        if(!jwtProvider.validateToken(token)) throw new InvalidTokenException(ResponseMessage.INVALID_TOKEN.getMessage());

        String userId = jwtProvider.getUserId(token);

        request.setAttribute("token", token);
        request.setAttribute("id", userId);

        return true;
    }
}
