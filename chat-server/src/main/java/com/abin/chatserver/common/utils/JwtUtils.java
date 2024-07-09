package com.abin.chatserver.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
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
     * 生成 token，默认有效期为 7 天
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

    public static boolean isTokenExpired(String token) {
        return extractAllClaims(token).get("exp").asDate().before(new Date());
    }

    private static Map<String, Claim> extractAllClaims(@NonNull String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaims();
        } catch (Exception e) {
            log.error("decode error, token: {}", token, e);
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
                .map(map -> map.get(UID_CLAIM))
                .map(Claim::asLong)
                .orElse(null);
    }


    public static void main(String[] args) {
        long a = 123L;
        System.out.println(generateToken(a));
        System.out.println(extractAllClaims(generateToken(a)));
    }
}
