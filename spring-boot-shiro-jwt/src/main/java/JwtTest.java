import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class JwtTest {
    public static void main(String[] args) {
        String token = null;
        // String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1SWQiOjExLCJleHAiOjE1MjU3ODYzNTEsImp0aSI6InRva2VuSWQifQ.sJiqurUBEAvuL5T9d1nbfcQ-wtcOfW_cqB2nGUlnNf4";
        int expireTimes = 5000;
        try {
            Date date = new Date(System.currentTimeMillis() + expireTimes);
            Algorithm algorithm = Algorithm.HMAC256("secret");
            token = JWT.create()
                    .withClaim("uId", 11)
                    .withJWTId("tokenId")
                    .withExpiresAt(date)
                    .sign(algorithm);
            System.out.println("token: " + token);
        } catch (UnsupportedEncodingException exception){
            //UTF-8 encoding not supported
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("jwt.getPayload: " + jwt.getClaim("uId").asInt());
            System.out.println("jwt.getId: " + jwt.getId());

        } catch (UnsupportedEncodingException exception){
            exception.printStackTrace();
            //UTF-8 encoding not supported
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            exception.printStackTrace();
        }
    }
}
