package com.onlinestore.buy.controllers;

import com.onlinestore.buy.requests.LoginRequest;
import com.onlinestore.buy.security.ShopUserDetailsService;
import com.onlinestore.buy.security.jwt.JWTUtils;
import com.onlinestore.buy.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${api.version}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JWTUtils jwtUtils;
    private final ShopUserDetailsService userDetailsService;
    private final CookieUtils cookieUtils;
    private final AuthenticationManager authenticationManager;
    @Value("${auth.token.refreshExpirationInMills}")
    private Long refreshTokenExpirationTime;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            String accessToken = jwtUtils.generateAccessToken(authentication);
            String refreshToken = jwtUtils.generateRefreshToken(authentication);
            cookieUtils.addRefreshTokenCookie(response, refreshToken, refreshTokenExpirationTime);
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            return ResponseEntity.status(HttpStatus.OK).body(tokens);
        } catch(AuthenticationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to authenticate user");
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
        cookieUtils.logCookies(request);
        String refreshToken = cookieUtils.getRefreshTokenFromCookies(request);
        if (refreshToken == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found");
        if(jwtUtils.validateAccessToken(refreshToken)) {
            String userName = jwtUtils.getEmailFromToken(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            String newAccessToken = jwtUtils.generateAccessToken(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
            if(newAccessToken != null) {
                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", newAccessToken);
                return ResponseEntity.status(HttpStatus.OK).body(tokens);
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate access token");
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid refresh token");
    }
}
