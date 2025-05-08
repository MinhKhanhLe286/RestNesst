package com.pbl5cnpm.airbnb_service.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;
import com.pbl5cnpm.airbnb_service.dto.Request.FavoriteRequest;
import com.pbl5cnpm.airbnb_service.dto.Request.UserProfileRequset;
import com.pbl5cnpm.airbnb_service.dto.Request.UserRequest;
import com.pbl5cnpm.airbnb_service.dto.Response.ApiResponse;
import com.pbl5cnpm.airbnb_service.dto.Response.UserFavoriteResponse;
import com.pbl5cnpm.airbnb_service.dto.Response.UserInfor;
import com.pbl5cnpm.airbnb_service.dto.Response.UserResponse;
import com.pbl5cnpm.airbnb_service.service.FavoriteService;
import com.pbl5cnpm.airbnb_service.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final FavoriteService favoriteService;
    private final UserService userService;


    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserResponse>> created(@RequestBody @Valid UserRequest request) {
        UserResponse userResponse = userService.handleCreateUser(request);

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code(201)
                .message("Created user successfully!")
                .result(userResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getALL() {
        List<UserResponse> userResponses = this.userService.handleGetAll();

        ApiResponse<List<UserResponse>> apiResponse = ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .message("Fetched users successfully!")
                .result(userResponses)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/users/myInformation")
    public ApiResponse getMethodName(HttpServletRequest request) throws ParseException, JOSEException {
        String authorization = request.getHeader("Authorization");
        String token = "";
        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }
        return ApiResponse.<UserInfor>builder()
                .result(this.userService.handleInfor(token))
                .build();
    }

    @GetMapping("/users/favorites") // lây full 
    public ApiResponse<UserFavoriteResponse> getMethodName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        var UserFavoriteResponse = this.userService.getFavorites(username);
        return ApiResponse.<UserFavoriteResponse>builder()
                .message("fetch favorite for user")
                .code(200)
                .result(UserFavoriteResponse)
                .build();
    }

    @PostMapping("/users/favorites") // tạo
    public ResponseEntity<Void> postMethodName(@RequestBody FavoriteRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long listingId = request.getListingId();
        this.favoriteService.addFavorite(listingId, username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/users/favorites") // xóa
    public ApiResponse<Void> handleDelete(@RequestBody FavoriteRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long listingId = request.getListingId();
        this.favoriteService.deleteFavorite(listingId, username);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("delete succesfully")
                .build();
    }

    @PutMapping(value = "/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UserInfor> updateUser(@ModelAttribute UserProfileRequset request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<UserInfor>builder()
                .message("Update success")
                .code(200)
                .result(this.userService.handleUpdateProfile(request, username))
                .build();
    }

}
