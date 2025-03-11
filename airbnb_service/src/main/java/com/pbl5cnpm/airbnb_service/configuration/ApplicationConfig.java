package com.pbl5cnpm.airbnb_service.configuration;


import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pbl5cnpm.airbnb_service.entity.AmenitesEntity;
import com.pbl5cnpm.airbnb_service.entity.CategoriesEntity;
import com.pbl5cnpm.airbnb_service.entity.RoleEntity;
import com.pbl5cnpm.airbnb_service.entity.UserEntity;
import com.pbl5cnpm.airbnb_service.enums.RoleName;
import com.pbl5cnpm.airbnb_service.repository.AmenitiesRepository;
import com.pbl5cnpm.airbnb_service.repository.CategoriesRepository;
import com.pbl5cnpm.airbnb_service.repository.RoleRepository;
import com.pbl5cnpm.airbnb_service.repository.UserRepository;
import com.pbl5cnpm.airbnb_service.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;


@Configuration
@FieldDefaults(level =  AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfig {
    
    RoleService roleService;
    RoleRepository roleRepository;
    PasswordEncoder encoder;
    CategoriesRepository categoriesRepository; 
    AmenitiesRepository amenitiesRepository;
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args ->{
            createBaseCategoies();
            createFullRole();
            createdBaseAmenities();
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
    private void createFullRole(){
        createRole(RoleName.ADMIN.name());
        createRole(RoleName.GUEST.name());
        createRole(RoleName.HOST.name());
    }
    private void createRole(String name){
        if(this.roleRepository.findByRoleName(name).isEmpty()){
            this.roleService.handleCreateRole(name);
        }
    }
    private void createBaseCategoies(){
        createBaseCategory("Công viên quốc gia","congvienquocgia.png",1);
        createBaseCategory("Biểu tượng","bieutuong.png",2);
        createBaseCategory("Thiết kế","thietke.png",3);
        createBaseCategory("Mới","moi.png",4);
        createBaseCategory("Vui chơi","vuichoi.png",5);
        createBaseCategory("Hướng biển","huongbien.png",6);
        createBaseCategory("Phòng","phong.png",7);
        createBaseCategory("Hồ bơi tuyệt đẹp","hoboituyetdep.png",8);
    }
    private void createBaseCategory(String name, String thumnailUrl, Integer position){
        if(this.categoriesRepository.findByName(name).isEmpty()){
            CategoriesEntity entity = CategoriesEntity.builder()
                                        .name(name)
                                        .thumnailUrl("/uploads/"+thumnailUrl)
                                        .deleted(false)
                                        .position(position)
                                        .build();
            this.categoriesRepository.save(entity);
        }
    }
    private void createdBaseAmenities(){
        createdBaseAmenity("Hướng nhìn ra vườn","huongnhinravuon.png");
        createdBaseAmenity("Hướng nhìn ra núi","huongnhinranuii.png");
        createdBaseAmenity("Bếp","bep.png");
        createdBaseAmenity("Chỗ đỗ xe miễn phí tại nơi ở","chodoxxemienphi.png");
        createdBaseAmenity("Máy giặt","maygiat.png");
        createdBaseAmenity("Wifi","output_wifi.png");
        createdBaseAmenity("Hồ bơi chung","hoboichung.png");
        createdBaseAmenity("Ti vi","tivi.png");
    }
    private void createdBaseAmenity(String name, String thumnailUrl){
        if(this.amenitiesRepository.findByName(name).isEmpty()){
            AmenitesEntity entity = AmenitesEntity.builder()
                                    .name(name)
                                    .thumnailUrl("/uploads/"+thumnailUrl)
                                    .build();
            this.amenitiesRepository.save(entity);
        }
    }
}
