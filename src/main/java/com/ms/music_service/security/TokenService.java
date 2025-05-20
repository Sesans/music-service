package com.ms.music_service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenService {
    @Value("${jwt.security.token}")
    private String secret;

    public DecodedJWT validateAndDecodeToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("API_userManagement")
                    .build()
                    .verify(token);
        }catch (JWTVerificationException exception){
            throw new RuntimeException("Error validating token!");
        }
    }
}