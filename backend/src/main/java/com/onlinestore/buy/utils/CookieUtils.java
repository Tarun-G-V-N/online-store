package com.onlinestore.buy.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CookieUtils {

    @Value("${app.useSecureCookie}")
    public boolean useSecureCookie;

    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken, Long maxAge) {
        if(response == null) throw new IllegalArgumentException("Response cannot be null");
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true); //Only http requests can access the cookie. Prevents xss attacks
        refreshTokenCookie.setSecure(useSecureCookie); //Only https requests can access the cookie. Prevents man in the middle attacks
        refreshTokenCookie.setPath("/"); //cookie is available on all paths
        refreshTokenCookie.setMaxAge((int) (maxAge / 1000)); //cookie expires after maxAge seconds
        String sameSite = useSecureCookie ? "None" : "Lax"; //Prevents CSRF attacks. Valid values are None, Lax, Strict
        setResponseHeader(response, refreshTokenCookie, sameSite);
    }

    public void setResponseHeader(HttpServletResponse response, Cookie cookie, String sameSite) {
        StringBuilder cookieHeader = new StringBuilder();
        cookieHeader.append(cookie.getName()).append("=").append(cookie.getValue());
        cookieHeader.append("; HttpOnly; Path=").append(cookie.getPath());
        cookieHeader.append("; Max-Age=").append(cookie.getMaxAge());
        cookieHeader.append(useSecureCookie ? "; Secure" : "");
        cookieHeader.append("; SameSite=").append(sameSite);
        response.addHeader("Set-Cookie", cookieHeader.toString());
    }

    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("Name of cookie found: "+cookie.getName());
                if (cookie.getName().equals("refreshToken")) return cookie.getValue();
            }
        }
        return null;
    }

    public void logCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        System.out.println("Cookies: "+ cookies != null ? Arrays.toString(cookies) : "null");
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("Cookie Name: "+cookie.getName()+", value: "+cookie.getValue());
            }
        }
    }
}
