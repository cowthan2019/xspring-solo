package com.danger.utils;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.cp4j.core.AssocArray;
import org.cp4j.core.Lang;

import java.util.Date;
import java.util.Map;

public class JWT {
    private static final String SECRET = "XX#$%()(#*!()!KL<><MQLMNQNK sdfrow32234545fdf>?N<:{LWPW";
    public static final long MAX_AGE = Lang.millis_for_1_year;

    private static final String EXP = "exp";

    private static final String PAYLOAD = "payload";

    //加密，传入一个对象和有效期
    public static String sign(AssocArray payload) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTCreator.Builder builder = com.auth0.jwt.JWT.create();

            if(payload != null){
                for (Map.Entry<String,Object> entry: payload.entrySet()){
                    builder.withClaim(entry.getKey(), entry.getValue() + "");
                }
            }
            builder.withIssuer("auth0")
                    .withSubject("subject")
                    .withExpiresAt(new Date(System.currentTimeMillis() + MAX_AGE));
            String token = builder.sign(algorithm);
            return token;
        } catch (JWTCreationException e){
            throw new RuntimeException(e);
        }
    }

    //解密，传入一个加密后的token字符串和解密后的类型
    public static AssocArray unsign(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm)
//                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            if(jwt != null && jwt.getToken() != null && jwt.getToken().equals(token)){
                return decode(token);
            }else{
                return null;
            }
        } catch (Exception e){
            if(e instanceof TokenExpiredException){
                AssocArray payload = AssocArray.array();
                payload.add("expired", 1);
                return payload;
            }else{
                return null;
            }
        }
    }

    public static AssocArray decode(String token){
//        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXUyJ9.eyJpc3MiOiJhdXRoMCJ9.AbIJTDMFc7yUa5MhvcP03nJPyCPzZtQcGEp-zWfOkEE";
        AssocArray payload = AssocArray.array();
        try {
            DecodedJWT jwt = com.auth0.jwt.JWT.decode(token);

            Map<String, Claim> claims = jwt.getClaims();    //Key is the Claim name
            if(claims != null){
                for (Map.Entry<String,Claim> entry: claims.entrySet()){
                    payload.add(entry.getKey(), entry.getValue().asString());
                }
            }
            if(jwt.getExpiresAt() != null){
                payload.add("expireAt", jwt.getExpiresAt().getTime());
            }
            return payload;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void test(String[] args) {
        String token = sign(AssocArray.array().add("userToken", "dsdfasdfsdf"));
        System.out.println(token);


        AssocArray payload = unsign(token);
        if(payload != null){
            System.out.println("token合法");
            if(payload.getInt("expired", 0) == 1){
                System.out.println("但是过期了");
            }else{
                System.out.println(payload);
            }

        }else{
            System.out.println("token不合法");
        }

//        unsign(token)
    }
}