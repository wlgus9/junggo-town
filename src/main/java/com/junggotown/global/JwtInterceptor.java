package com.junggotown.global;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("인터셉터 실행");

        String token = jwtProvider.resolveToken(request);

        String url = request.getRequestURI();
        log.info("token = {}", token);

        if(token == null) throw new JwtException("토큰이 없습니다.");

        if(!jwtProvider.validateToken(token)) throw new JwtException("토큰이 유효하지 않습니다.");

        String userId = jwtProvider.getUserId(token);

        request.setAttribute("token", token);
        request.setAttribute("id", userId);

        return true;
    }
}
