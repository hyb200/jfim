package com.abin.chatserver.common.filter;

import com.abin.chatserver.common.domain.enums.ErrorEnum;
import com.abin.chatserver.common.domain.vo.BaseResponse;
import com.abin.chatserver.common.utils.JsonUtils;
import com.abin.chatserver.common.utils.JwtUtils;
import com.abin.chatserver.user.dao.UserDao;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDao userDao;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            Long uid = JwtUtils.extractUidOrNull(token);
            if (Objects.nonNull(uid) &&
                    Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                UserDetails user = userDao.getById(uid);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, null);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }  catch (TokenExpiredException e) {
            log.error("Token decode error, {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            BaseResponse resp = BaseResponse.error(ErrorEnum.TOKEN_EXPIRE_ERROR);
            response.getWriter().write(JsonUtils.toStr(resp));
            return;
        }
        filterChain.doFilter(request, response);
    }
}
