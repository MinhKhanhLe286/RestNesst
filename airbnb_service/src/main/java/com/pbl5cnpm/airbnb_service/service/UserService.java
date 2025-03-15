package com.pbl5cnpm.airbnb_service.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pbl5cnpm.airbnb_service.dto.Request.UserRequest;
import com.pbl5cnpm.airbnb_service.dto.Response.UserResponse;
import com.pbl5cnpm.airbnb_service.entity.RoleEntity;
import com.pbl5cnpm.airbnb_service.entity.UserEntity;
import com.pbl5cnpm.airbnb_service.enums.RoleName;
import com.pbl5cnpm.airbnb_service.exception.AppException;
import com.pbl5cnpm.airbnb_service.exception.ErrorCode;
import com.pbl5cnpm.airbnb_service.mapper.UserMapper;
import com.pbl5cnpm.airbnb_service.repository.RoleRepository;
import com.pbl5cnpm.airbnb_service.repository.UserRepository;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final MailerService mailerService;
    private final PasswordEncoder passwordEncoder; 

    public UserResponse handleCreateUser(UserRequest request) {
        // Kiểm tra username đã tồn tại chưa
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        try {
            String name = request.getFullname();
            mailerService.sendHtmlEmail(request.getEmail(), "Welcome to Airbnb", "Thank " + name + " for choosing our service!");
        } catch (MessagingException e) {
            log.error("Send mail fail!");
            e.printStackTrace();
        }

        // Mã hóa password bằng PasswordEncoder thay vì tạo mới BCryptPasswordEncoder
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(roleRepository.findByRoleName(RoleName.GUEST.name())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED)));

        // Chuyển đổi UserRequest thành UserEntity
        UserEntity userEntity = mapper.toUserEntity(request);
        userEntity.setPassword(encodedPassword);
        userEntity.setRoles(roles);

        // Lưu vào database và trả về UserResponse
        return mapper.toUserResponse(userRepository.save(userEntity));
    }

    public List<UserResponse> handleGetAll(){
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
            .map(mapper::toUserResponse)
            .collect(Collectors.toList());
    }
}
