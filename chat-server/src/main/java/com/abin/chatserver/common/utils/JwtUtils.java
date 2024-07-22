package com.abin.chatserver.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * JWT Token的生成与解析
 */
@Slf4j
@Component
public class JwtUtils {

    private static final String SECERT = "dfsjakl;fdsa@fas";

    public static final Algorithm ALGORITHM = Algorithm.HMAC256(SECERT);

    private static final String UID_CLAIM = "uid";

    /**
     * 生成 token，默认有效期为 1 天
     * @param uid
     * @return
     */
    public static String generateToken(Long uid) {
        return JWT.create()
                .withClaim(UID_CLAIM, uid)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .sign(ALGORITHM);
    }

    private static Map<String, Claim> extractAllClaims(@NonNull String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaims();
        } catch (TokenExpiredException e) {
            log.error("Decode error. {}", e.getMessage());
        }
        return null;
    }

    /**
     * 获取uid
     * @param token
     * @return
     */
    public static Long extractUidOrNull(String token) {
        return Optional.ofNullable(extractAllClaims(token))
                .filter(map -> map.get("exp").asDate().after(new Date()))
                .map(map -> map.get(UID_CLAIM))
                .map(Claim::asLong)
                .orElse(null);
    }


    public static void main(String[] args) {
        long a = 10000L;
        String token = generateToken(a);
        System.out.println(extractAllClaims(token));
        System.out.println(extractUidOrNull(token));
    }
}
