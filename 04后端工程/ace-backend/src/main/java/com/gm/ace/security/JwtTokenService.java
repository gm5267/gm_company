package com.gm.ace.security;

import com.gm.ace.common.exception.BizException;
import com.gm.ace.common.exception.SystemException;
import com.gm.ace.common.result.ResultCode;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * JWT 签发与解析（Nimbus JOSE，HS256）
 *
 * @author guoym
 */
@Service
public class JwtTokenService {

    private final JwtProperties props;
    private JWSSigner signer;
    private JWSVerifier verifier;

    public JwtTokenService(JwtProperties props) {
        this.props = props;
    }

    @PostConstruct
    public void init() {
        byte[] key = props.getSecret().getBytes(StandardCharsets.UTF_8);
        try {
            this.signer = new MACSigner(key);
            this.verifier = new MACVerifier(key);
        } catch (JOSEException e) {
            throw new SystemException("JWT 密钥长度不足（需 >= 256bit）", e);
        }
    }

    public String getSecret() {
        return props.getSecret();
    }

    /** 签发令牌 */
    public String issue(Long userId, String username, Long tenantId, List<String> roles) {
        Date exp = new Date(System.currentTimeMillis() + props.getExpireSeconds() * 1000L);
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("ace-backend")
                .expirationTime(exp)
                .claim("uid", userId)
                .claim("tid", tenantId)
                .claim("roles", roles == null ? List.of() : roles)
                .build();
        com.nimbusds.jose.JWSObject jws = new com.nimbusds.jose.JWSObject(
                new JWSHeader(JWSAlgorithm.HS256), new Payload(claims.toString()));
        try {
            jws.sign(signer);
        } catch (JOSEException e) {
            throw new SystemException("令牌签发失败", e);
        }
        return jws.serialize();
    }

    /** 解析并校验令牌（校验签名与有效期由底层完成） */
    public JWTClaimsSet parse(String token) {
        try {
            com.nimbusds.jose.JWSObject jws = com.nimbusds.jose.JWSObject.parse(token);
            if (!jws.verify(verifier)) {
                throw new BizException(ResultCode.UNAUTHORIZED, "令牌签名无效");
            }
            return JWTClaimsSet.parse(jws.getPayload().toString());
        } catch (Exception e) {
            throw new BizException(ResultCode.UNAUTHORIZED, "令牌解析失败");
        }
    }
}
