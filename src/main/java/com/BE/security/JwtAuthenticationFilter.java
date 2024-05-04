package com.BE.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        String jwt = null;
        final String userEmail;
        final Cookie[] cookies = request.getCookies();

        if(cookies == null){
            filterChain.doFilter(request, response);
            return;
        }

        for(Cookie c : cookies){
            if(c.getName().equals("biskuat")){
                jwt = c.getValue();
                break;
            }
        }

        if(jwt != null) {
            // jwt = authorizationHeader.substring(7);
            try{
                userEmail = jwtService.extractUsername(jwt);
            } catch (Exception e){
                response
                    .addHeader("Set-Cookie", ResponseCookie.from("biskuat", "")
                    .httpOnly(true)
                    .path("/")
                    .maxAge(0)
                    .build().toString());
                filterChain.doFilter(request, response);
                return;
            }

            if(userEmail != null || SecurityContextHolder.getContext().getAuthentication() == null){
                List<SimpleGrantedAuthority> roles = jwtService.extractRoles(jwt);

                if(jwtService.isTokenValid(jwt, userEmail)){
                    // set authentication
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userEmail, null, roles);
                    authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } 
            }
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

}
