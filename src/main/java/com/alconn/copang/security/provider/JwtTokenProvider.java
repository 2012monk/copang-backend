package com.alconn.copang.security.provider;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.Role;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
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
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtTokenProvider {

    private final int exp = 60 * 60 * 24;

    private final String issuer = "alconn.co";

    private final String ACCESS_TOKEN = "access_token";

    private final String REFRESH_TOKEN = "refresh_token";

    private final ObjectMapper mapper;

    private final int refExp = 60 * 60 * 24 * 3;

    @Value("${spring.jwt.secret}")
    private String keyString;

    private SecretKey key;

    @PostConstruct
    protected void init() {
        key = new SecretKeySpec(Base64.encodeBase64(keyString.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256.getJcaName());
    }

    public String createRefreshToken(Client user) {
        Date now = new Date();

        Date exp = new Date(now.getTime() + refExp);

        Claims claims = Jwts.claims();
        claims.put("uid", user.getClientId());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(REFRESH_TOKEN)
                .setIssuedAt(now)
                .setExpiration(exp)
                .setAudience(user.getUsername())
                .signWith(key)
                .compact();
    }

    public boolean validateRefreshToken(String token) throws InvalidTokenException {
        try{
            resolveRefreshToken(token);
            return true;
        }catch (Exception e){
            log.debug("invalid refresh token", e);
            throw new InvalidTokenException();
        }
    }

    public OptionalLong getUserIdFromRefreshToken(String token) {
        Jws<Claims> claimsJws = resolveRefreshToken(token);
        Long id = claimsJws.getBody().get("uid", long.class);
        return OptionalLong.of(id);
    }


    public Optional<String> getUsernameFromRefreshToken(String token) {
        Jws<Claims> claimsJws = resolveRefreshToken(token);
        String username = claimsJws.getBody().getAudience();
        return Optional.ofNullable(username);
    }

    private Jws<Claims> resolveRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .requireIssuer(issuer)
                .requireSubject(REFRESH_TOKEN)
                .build()
                .parseClaimsJws(token);
    }

    public Optional<String> createAccessToken(Client user) {
        Date now = new Date();

        Date expDate = new Date(now.getTime() + exp);


        Claims claims = Jwts.claims();
        claims.put("user", user);
//        Serializer<Map<String, Client>> serializer = Maps.
        return Optional.of(Jwts.builder()
                .serializeToJsonWith(new JacksonSerializer<>(mapper))
//                .serializeToJsonWith(new JacksonSerializer<Map<String, ?>>(Maps.of("user", Client.class).build()))
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
//                    .deserializeJsonWith(new JacksonDeserializer(Maps.of("user", Client.class).build()))
//                    .deserializeJsonWith(new JacksonDeserializer<>(mapper))
                    .setSigningKey(key)
                    .requireIssuer(issuer)
                    .requireSubject(ACCESS_TOKEN)
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException e) {
            log.info("invalid token{}", e.getMessage());
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
        try {
            claims = getTokenBody(token);
            Claims finalClaims = claims;
            claims.keySet().forEach(c -> log.warn(c + finalClaims.get(c).toString()));
            Map json = (Map) claims.get("user");
//            String json = (String) claims.get("user");
//            log.warn(json);
            user = mapper.convertValue(json, Client.class);
//            user = claims.get("user", Client.class);
        } catch (Exception e) {
            log.info("invalid token Exception :  {},  {}",e.getClass().getSimpleName(), e.getMessage());
        }
        return Optional.ofNullable(user);
    }
}
