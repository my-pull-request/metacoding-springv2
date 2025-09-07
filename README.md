# 동영상 강의에 소개할 내용

## 개념정리

- Exception Handler 예외처리 vs Filter의 예외처리
- CSRF
- CORS (filter로 구현, 최신 코드 구현 설명)
- Security의 세션 (스레드방식, 세션방식)

## 1. CORS Options Preflight

```java
package com.metacoding.springv2.core.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String origin = request.getHeader("Origin");
        log.debug("Origin : " + origin);

        response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Key, Content-Type, Accept, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // Preflight 요청을 허용하고 바로 응답하는 코드
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(200);
        } else {
            chain.doFilter(req, res);
        }
    }
}
```