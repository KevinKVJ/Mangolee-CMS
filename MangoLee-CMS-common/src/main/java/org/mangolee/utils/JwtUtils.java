package org.mangolee.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.mangolee.entity.UserInfo;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

// Jwt工具类 负责生成token 验证token
public class JwtUtils {

    // 签名算法
    private final static SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    // secret
    private final static String SECRET_KEY = "hHnmgpUrSxNMbepoW5GPS/rumwzxMj+jGx/D0in5dDU=";

    // 通过UserInfo生成token
    public static String createToken(UserInfo userInfo) {

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key    signingkey = new SecretKeySpec(apiKeySecretBytes, SIGNATURE_ALGORITHM.getJcaName());

        // 创建并返回token
        return Jwts.builder()
                .setSubject(userInfo.getUsername())
                .claim("id", userInfo.getId())
                .claim("username", userInfo.getUsername())
                .claim("email", userInfo.getEmail())
                .claim("role", userInfo.getRole())
                .claim("uuid", userInfo.getUuid())
                .claim("gmtCreate", userInfo.getGmtCreate())
                .signWith(SIGNATURE_ALGORITHM, signingkey)
                .compact();
    }

    // 通过token生成UserInfo
    public static UserInfo getUserInfo(String token) {

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key    signingkey = new SecretKeySpec(apiKeySecretBytes, SIGNATURE_ALGORITHM.getJcaName());

        Claims claims = Jwts.parser()
               .setSigningKey(signingkey)
                .parseClaimsJws(token)
                .getBody();
        UserInfo userInfo = new UserInfo();
        userInfo.setId(claims.get("id", Long.class));
        userInfo.setUsername(claims.get("username", String.class));
        userInfo.setEmail(claims.get("email", String.class));
        userInfo.setRole(claims.get("role", String.class));
        userInfo.setUuid(claims.get("uuid", String.class));
        userInfo.setGmtCreate(claims.get("gmtCreate", Date.class));
        return userInfo;
    }
}
