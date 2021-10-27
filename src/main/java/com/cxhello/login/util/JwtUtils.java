package com.cxhello.login.util;

import com.cxhello.login.constant.JwtKeyConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Map;

/**
 * @author cxhello
 * @date 2021/10/26
 */
@Component
public class JwtUtils {

    // 令牌自定义标识
    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.secret}")
    private String secret;

    private Key getKey() {
        return new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS512.getJcaName());
    }

    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS512).compact();
    }

    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.isNotBlank(token) && token.startsWith(JwtKeyConstants.TOKEN_PREFIX)) {
            token = token.replace(JwtKeyConstants.TOKEN_PREFIX, "");
        }
        return token;
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
