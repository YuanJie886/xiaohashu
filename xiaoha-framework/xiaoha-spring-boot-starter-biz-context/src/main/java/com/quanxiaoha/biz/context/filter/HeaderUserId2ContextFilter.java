package com.quanxiaoha.biz.context.filter;

import com.quanxiaoha.biz.context.holder.LoginUserContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class HeaderUserId2ContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userId = request.getHeader("userId");
        log.info("## userId: {}", userId);
        LoginUserContextHolder.setUserId(userId);
//        LoginUserContextHolder.remove();
        filterChain.doFilter(request, response);
    }
}
