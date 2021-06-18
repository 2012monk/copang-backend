package com.alconn.copang.security.privider;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.Role;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.lang.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String keyString = "asdlfkajsdlfkjalksdjfaklsjdfkljaskldjfljk123123";

    private final int exp = 60 * 60 * 15;

    private final String issuer = "alconn.co";

    private final String ACCESS_TOKEN = "access_token";

    private final ObjectMapper mapper;


    private SecretKey key;

    @PostConstruct
    protected void init() {
        key = new SecretKeySpec(Base64.encodeBase64(keyString.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256.getJcaName());
    }

    public Optional<String> createAccessToken(Client user) {
        Date now = new Date();

        Date expDate = new Date(now.getTime() + exp);


        Claims claims = Jwts.claims();
        claims.put("user", user);
        return Optional.of(Jwts.builder()
                .setClaims(claims)
                .setIssuer("alconn.co")
                .setSubject(ACCESS_TOKEN)
                .setAudience(user.getUsername())
                .setExpiration(expDate)
                .setIssuedAt(now)
                .signWith(key)
                .compact());
    }

    public Optional<String> createGuestToken() {
        return createAccessToken(Client.builder().role(Role.GUEST).build());
    }

    public void validateAccessToken(String s) throws InvalidTokenException {
        resolveToken(s);
    }

    public JwtTokenProvider validateAccessTokenChain(String s) throws InvalidTokenException {
        resolveToken(s);
        return this;
    }

    public Jws<Claims> resolveToken(String token) throws InvalidTokenException {
        try {
            return Jwts.parserBuilder()
                    .deserializeJsonWith(new JacksonDeserializer(Maps.of("user", Client.class).build()))
                    .setSigningKey(key)
                    .requireIssuer(issuer)
                    .requireSubject(ACCESS_TOKEN)
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException e) {
            e.printStackTrace();
            throw new InvalidTokenException();
        }
    }

    public Claims getTokenBody(String token) throws InvalidTokenException {
        return resolveToken(token).getBody();
    }

    public JwsHeader getHeader(String token) throws InvalidTokenException {
        return resolveToken(token).getHeader();
    }

    public Optional<Client> resolveUserFromToken(String token) {
        Claims claims = null;
        Client user = null;
        try{
            claims = getTokenBody(token);
            user = claims.get("user", Client.class);
        }catch (Exception  e){
            log.info("invalid token", e);
        }
        return Optional.ofNullable(user);
    }
}
