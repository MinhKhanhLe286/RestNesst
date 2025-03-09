package com.pbl5cnpm.airbnb_service.configuration;


import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pbl5cnpm.airbnb_service.entity.RoleEntity;
import com.pbl5cnpm.airbnb_service.entity.UserEntity;
import com.pbl5cnpm.airbnb_service.enums.RoleName;
import com.pbl5cnpm.airbnb_service.repository.RoleRepository;
import com.pbl5cnpm.airbnb_service.repository.UserRepository;
import com.pbl5cnpm.airbnb_service.service.RoleService;

import lombok.extern.slf4j.Slf4j;


@Configuration
@Slf4j
public class ApplicationConfig {
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args ->{
            createFull();
            if(userRepository.findByUsername("admin").isEmpty()){
                
                
                Set<RoleEntity> roles = new HashSet<>(this.roleRepository.findAll());
                
                UserEntity userOrigin = UserEntity.builder()    
                                        .username("admin")
                                        .password(encoder.encode("12345678"))
                                        .roles(roles)
                                        .build();
                userRepository.save(userOrigin);
                log.warn("User admin created with full Role");
            }
        };
    }
    private void createFull(){
        createRole(RoleName.ADMIN.name());
        createRole(RoleName.GUEST.name());
        createRole(RoleName.HOST.name());
    }
    private void createRole(String name){
        if(this.roleRepository.findByRoleName(name).isEmpty()){
            this.roleService.handleCreateRole(name);
        }
    }
}
