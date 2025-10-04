package com.example.user_srv.controller;

import com.example.user_srv.model.ApiResponse;
import com.example.user_srv.model.dto.request.AuthenticationRequest;
import com.example.user_srv.model.dto.request.RefreshRequest;
import com.example.user_srv.model.dto.request.ValidTokenRequest;
import com.example.user_srv.model.dto.response.AuthenticationResponse;
import com.example.user_srv.model.dto.response.ValidTokenResponse;
import com.example.user_srv.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request, Optional.empty());
        return ApiResponse.<AuthenticationResponse>builder().data(result).build();
    }

    @PostMapping("/valid")
    ApiResponse<ValidTokenResponse> valid(@RequestBody ValidTokenRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.verifyToken(request);
        return ApiResponse.<ValidTokenResponse>builder().data(result).build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder().data(result).build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(HttpServletRequest servletRequest) throws ParseException, JOSEException {
        authenticationService.logout(servletRequest);
        return ApiResponse.<Void>builder().build();
    }
}
