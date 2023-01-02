package cn.liyu.security.security;

import cn.liyu.security.model.JwtUser;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cn.liyu.security.constant.SecurityConstant.*;


public class TokenFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(TokenFilter.class);


    private final TokenProvider tokenProvider;
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * @param tokenProvider       Token
     * @param stringRedisTemplate 缓存
     */
    public TokenFilter(TokenProvider tokenProvider, StringRedisTemplate stringRedisTemplate) {
        this.tokenProvider = tokenProvider;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = resolveToken(request);
        // 对于 Token 为空的不需要去查 Redis
        if (StringUtils.isNotBlank(token)) {
            JwtUser jwtUser = null;
//            String jwt = null;
            try {
//                jwt = stringRedisTemplate.opsForValue().get(ONLINE_TOKEN_KEY + token);
                String s = stringRedisTemplate.opsForValue().get(ONLINE_USER_KEY + token);
                if (StringUtils.isNotBlank(s)) {
                    jwtUser = JSON.parseObject(s, JwtUser.class);
                }
            } catch (ExpiredJwtException e) {
                log.error(e.getMessage());
//                stringRedisTemplate.delete(ONLINE_TOKEN_KEY + token);
                stringRedisTemplate.delete(ONLINE_USER_KEY + token);
            }

            if (jwtUser != null) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                // Token 续期
                tokenProvider.checkRenewal(token);
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * 初步检测Token
     *
     * @param request /
     * @return /
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER);
        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith(TOKEN_TYPE)) {
            // 去掉令牌前缀
            return bearerToken.replace(TOKEN_TYPE, "");
        } else {
            log.debug("非法Token：{}", bearerToken);
        }
        return null;
    }
}
