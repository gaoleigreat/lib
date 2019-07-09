package com.lego.survey.lib.web.utils;

import com.lego.survey.lib.web.property.JwtProperty;
import com.survey.lib.common.vo.AuthVo;
import com.survey.lib.common.vo.CurrentVo;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * @author yanglf
 * @description
 * @since 2019/7/9
 **/
@Component
public class JWTHelper {

    @Autowired
    private JwtProperty jwtProperty;

    /**
     * 生成用户 token
     * @param currentVo
     * @param deviceType
     * @return
     */
    public  AuthVo generateUserToken(CurrentVo currentVo,String deviceType){
        // 使用加密算法  HS256
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date nowDate = new Date(nowMillis);
        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtProperty.getBase64Secret());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        AuthVo authVo = new AuthVo();
        authVo.setIssUer(jwtProperty.getClientId());
        authVo.setAudience(jwtProperty.getName());
        authVo.setSubject(deviceType);
        authVo.setCurrentVo(currentVo);

        JwtBuilder jwtBuilder = Jwts.builder().setHeaderParam("type", "JWT")
                .claim("current", currentVo)
                // 设置 jwt 的签发者
                .setIssuer(jwtProperty.getClientId())
                // 设置 接收 jwt 的名称
                .setAudience(jwtProperty.getName())
                //  设置  jwt 所面向的对象
                .setSubject(deviceType)
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        int expiresSecond = jwtProperty.getExpiresSecond();
        Date exp;
        if (expiresSecond >= 0) {
            long expMillis = nowMillis + (expiresSecond * 1000);
            exp = new Date(expMillis);
            // 设置  jwt  的过期时间
            jwtBuilder.setExpiration(exp)
                    // 如果当前时间在 nowDate 之前  token不生效
                    .setNotBefore(nowDate);
            authVo.setExpiration(exp);
            authVo.setNotBefore(nowDate);
        }
        //  头部(Header)、载荷(Payload)与签名(Signature)
        String token = jwtBuilder.compact();
        authVo.setToken(token);
        return authVo;
    }


}
