package com.pbl5cnpm.airbnb_service.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.pbl5cnpm.airbnb_service.dto.Request.AuthenticationResquest;
import com.pbl5cnpm.airbnb_service.dto.Request.IntrospectRequest;
import com.pbl5cnpm.airbnb_service.dto.Request.LogoutRequest;
import com.pbl5cnpm.airbnb_service.dto.Response.AuthenticationResponse;
import com.pbl5cnpm.airbnb_service.dto.Response.IntrospectResponse;
import com.pbl5cnpm.airbnb_service.entity.InvalidTokenEntity;
import com.pbl5cnpm.airbnb_service.entity.RoleEntity;
import com.pbl5cnpm.airbnb_service.entity.UserEntity;
import com.pbl5cnpm.airbnb_service.exception.AppException;
import com.pbl5cnpm.airbnb_service.exception.ErrorCode;
import com.pbl5cnpm.airbnb_service.repository.InvalidTokenRepository;
import com.pbl5cnpm.airbnb_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final InvalidTokenRepository invalidTokenRepository;
    @Value("${security.secret}")
    private String SIGNER_KEY;
    @Value("${security.duration_access}")
    private String DURATION_ACCESS;
    @Value("${security.duration_refresh}")
    private String DURATION_REFRESH;

    /**
     * hanlde login 
     * @param resquest
     * @return
     * @throws JOSEException
     */
    public AuthenticationResponse authenticate(AuthenticationResquest resquest) throws JOSEException{
        UserEntity userEntity = this.userRepository.findByUsername(resquest.getUsername())
                                .orElseThrow(()-> new AppException(ErrorCode.USER_EXISTED));
        Boolean status = this.passwordEncoder.matches(resquest.getPassword(),userEntity.getPassword());
        if (!status){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }   
        String access_token = generationToken(userEntity,false);
        String refresh_token = generationToken(userEntity, true);
        

        return AuthenticationResponse.builder()
                                      .access_token(access_token)
                                      .refresh_token(refresh_token)
                                      .expired_token(DURATION_ACCESS)               
                                      .build();
    }
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException{
        isValidToken(request.getToken());
        String idToken = getClaimSet(request.getToken()).getJWTID();
        if (this.invalidTokenRepository.findById(idToken).isPresent()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return IntrospectResponse.builder().vaild(true).build();
    }
    private String generationToken(UserEntity userEntity, boolean isRefresh) throws JOSEException {
        Date now = new Date();
        long durationInMillis = isRefresh 
                ? Integer.parseInt(DURATION_REFRESH.trim()) * 1000L 
                : Integer.parseInt(DURATION_ACCESS.trim()) * 1000L;
        Date expiration = new Date(now.getTime() + durationInMillis);
        String typeToken = (isRefresh) ? "refresh" : "access";
        Set<String> roles = getRole(userEntity.getRoles());
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userEntity.getUsername())
                .jwtID(UUID.randomUUID().toString())
                .issuer("inmobi.com.vn")
                .issueTime(now)
                .expirationTime(expiration)
                .claim("type_token", typeToken)
                .claim("scope", buildScope(roles))
                .build();
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        JWSSigner jwsSigner = new MACSigner(SIGNER_KEY.getBytes());
        signedJWT.sign(jwsSigner);
    
        return signedJWT.serialize();
    }
    public AuthenticationResponse handleRefreshToken(String refreshToken) throws JOSEException{
        boolean isValid = isValidToken(refreshToken); // checked expired time
        boolean isRefreshToken = getClaimSet(refreshToken).getClaim("type_token").toString().equals("refresh");
        if (isValid && isRefreshToken){
            UserEntity user = this.userRepository.findByUsername(getClaimSet(refreshToken).getSubject())
                                        .orElseThrow(()->  new AppException(ErrorCode.USER_NOT_EXISTED));
            return AuthenticationResponse.builder()
                        .access_token(generationToken(user, false))
                        .refresh_token(generationToken(user, true))
                        .expired_token(DURATION_ACCESS)   
                        .build();
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    private String buildScope(Set<String> roles){
        StringJoiner joiner = new StringJoiner(" ", "", "");
        for (String item : roles) {
            joiner.add(item);
        }
        return joiner.toString();
    }
    private Set<String> getRole(Set<RoleEntity> entities){
        Set<String> result = new HashSet<>();
        for (RoleEntity roleEntity : entities) {
            result.add(roleEntity.getRoleName());
        }
        return result;
    }
    private boolean isValidToken(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY);
            boolean verified = jwt.verify(verifier);
    
            Date expirationTime = jwt.getJWTClaimsSet().getExpirationTime();
            Date now = new Date();
            if (verified && expirationTime != null && expirationTime.after(now)) {
                return true;
            }
            
        } catch (ParseException | JOSEException e) {
            e.printStackTrace(); 
        }
    
        return false;
    }
    public void handleLogout(LogoutRequest logoutRequest){
        invalidateToken(logoutRequest.getAccess_token());
        invalidateToken(logoutRequest.getRefresh_token());
    }
    private void invalidateToken (String token){
        if(isValidToken(token)){
            JWTClaimsSet claimsSet = getClaimSet(token);
            InvalidTokenEntity entity = InvalidTokenEntity.builder()
                                        .id(claimsSet.getJWTID())
                                        .expired(claimsSet.getExpirationTime())
                                        .build();
            this.invalidTokenRepository.save(entity);
        }
    }
    private JWTClaimsSet getClaimSet(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token); 
            return jwt.getJWTClaimsSet(); 
        } catch (ParseException e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }
    
}
