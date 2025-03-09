package com.pbl5cnpm.airbnb_service.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pbl5cnpm.airbnb_service.Exception.AppException;
import com.pbl5cnpm.airbnb_service.Exception.ErrorCode;
import com.pbl5cnpm.airbnb_service.dto.Request.UserRequest;
import com.pbl5cnpm.airbnb_service.dto.Response.UserResponse;
import com.pbl5cnpm.airbnb_service.entity.RoleEntity;
import com.pbl5cnpm.airbnb_service.entity.UserEntity;
import com.pbl5cnpm.airbnb_service.enums.RoleName;
import com.pbl5cnpm.airbnb_service.mapper.UserMapper;
import com.pbl5cnpm.airbnb_service.repository.RoleRepository;
import com.pbl5cnpm.airbnb_service.repository.UserRepository;

import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    RoleRepository roleRepository;
    UserRepository userRepository;
    UserMapper mapper;
    PasswordEncoder encoder;
    MailerService mailerService;

    public UserResponse handleCreateUser(UserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        // try {
        //     String name = request.getFullname();
        //      this.mailerService.sendHtmlEmail(request.getEmail(), "Wlecome to Airbnb", "Thank " + name + " for choosing service!");
        // } catch (MessagingException e) {
        //     log.error("Send mail fail!");
        //     e.printStackTrace();
        // }
        String pass = encoder.encode(request.getPassword());

        Set<RoleEntity> roles = new HashSet<>();
                        roles.add(this.roleRepository.findByRoleName(RoleName.GUEST.name())
                                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED)));
        UserEntity  userEntity = mapper.toUserEntity(request);
                    userEntity.setPassword(pass);
                    userEntity.setRoles(roles);

        return mapper.toUserResponse(this.userRepository.save(userEntity));
    }
    public List<UserResponse> handleGetAll(){
        List<UserEntity> users = this.userRepository.findAll();
        return users.stream()
            .map(mapper::toUserResponse)
            .collect(Collectors.toList());
    }
}
