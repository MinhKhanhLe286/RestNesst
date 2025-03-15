package com.pbl5cnpm.airbnb_service.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.pbl5cnpm.airbnb_service.dto.Request.AuthenticationResquest;
import com.pbl5cnpm.airbnb_service.dto.Request.IntrospectRequest;
import com.pbl5cnpm.airbnb_service.dto.Response.AuthenticationResponse;
import com.pbl5cnpm.airbnb_service.dto.Response.IntrospectResponse;
import com.pbl5cnpm.airbnb_service.entity.UserEntity;
import com.pbl5cnpm.airbnb_service.exception.AppException;
import com.pbl5cnpm.airbnb_service.exception.ErrorCode;
import com.pbl5cnpm.airbnb_service.repository.UserRepository;

@Service

public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Value("${security.secret}")
    protected String SIGNER_KEY;
    public AuthenticationService(UserRepository userRepository,PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public AuthenticationResponse authenticate(AuthenticationResquest resquest){
        UserEntity userEntity = this.userRepository.findByUsername(resquest.getUsername())
                                .orElseThrow(()-> new AppException(ErrorCode.USER_EXISTED));
        Boolean status = this.passwordEncoder.matches(resquest.getPassword(),userEntity.getPassword());
        if (!status){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }   
        String token = generationToken(resquest);

        return AuthenticationResponse.builder()
                                      .authenticated(true)
                                      .token(token)                 
                                      .build();
    }
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException{
        String token = request.getToken();
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY);
        SignedJWT jwt = SignedJWT.parse(token);
        Boolean verifed = jwt.verify(jwsVerifier);
        Date expityTime = jwt.getJWTClaimsSet().getExpirationTime();
        return IntrospectResponse.builder()
                                .vaild(verifed && expityTime.after(new Date()))
                                .build();
    }
    private String generationToken(AuthenticationResquest resquest){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                                .subject(resquest.getUsername())
                                .issuer("leminhkhanh")
                                .issueTime(new Date())
                                .expirationTime(new Date(
                                    Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                                ))
                                .claim("My app", "service airbnb")
                                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return null;
    }
}
