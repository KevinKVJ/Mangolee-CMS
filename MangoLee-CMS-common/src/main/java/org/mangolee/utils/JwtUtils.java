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
    public final static SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    // secret
    public final static String SECRET_KEY = "hHnmgpUrSxNMbepoW5GPS/rumwzxMj+jGx/D0in5dDU=";

    // 通过UserInfo生成token
    public static String createTokenFromUserInfo(UserInfo userInfo, String secretKey, SignatureAlgorithm signatureAlgorithm) {

        if (userInfo == null || secretKey == null || signatureAlgorithm == null) {
            return null;
        }

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key    signingkey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        // 创建并返回token
        return Jwts.builder()
                .setSubject(userInfo.getUsername())
                .claim("id", userInfo.getId())
                .claim("username", userInfo.getUsername())
                .claim("email", userInfo.getEmail())
                .claim("role", userInfo.getRole())
                .claim("uuid", userInfo.getUuid())
                .claim("gmtCreate", userInfo.getGmtCreate())
                .signWith(signatureAlgorithm, signingkey)
                .compact();
    }

    // 通过token生成UserInfo
    public static UserInfo getUserInfoFromToken(String token, String secretKey, SignatureAlgorithm signatureAlgorithm) {

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key    signingkey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        // 解析token抽取UserInfo
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
