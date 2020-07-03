package com.ht.dynamic.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PublicKey;

/**
 * 生成公钥
 * keytool -list -rfc -keystore oauth2.jks -storepass oauth2
 */
@Configuration
public class TokenConfig {

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        try {
            String jksPath = "C:\\Users\\qp\\oauth2.jks"; //jks file path
            String jksPassword = "oauth2"; // jks keyStore password
            String certAlias = "oauth2"; // cert alias
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(jksPath), jksPassword.toCharArray());
            PublicKey publicKey = keyStore.getCertificate(certAlias).getPublicKey();
            StringBuffer k = new StringBuffer();
            k.append("-----BEGIN PUBLIC KEY-----\n");
            String encoded = new BASE64Encoder().encode(publicKey.getEncoded());
            k.append(encoded);
            k.append("-----END PUBLIC KEY-----\n");
            jwtAccessTokenConverter.setVerifierKey(k.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        //使用公钥解密
        return jwtAccessTokenConverter;
    }

    @Bean
    public TokenStore tokenStore(){
        //jwt 管理令牌
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


}
