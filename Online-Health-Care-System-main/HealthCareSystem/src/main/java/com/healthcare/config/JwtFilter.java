package com.healthcare.config;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String path = request.getServletPath();
		System.out.println("PATH : "+path);
		if(path.startsWith("/api/auth")){
			filterChain.doFilter(request, response);
			return;
		}
		System.out.println("hello2");
		String header = request.getHeader("Authorization");
		if (header == null || !header.toLowerCase().startsWith("bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String token = header.substring(7).trim();
		try {
				System.out.println("hello3");
				String username = jwtUtil.extractUsername(token);
				String role = jwtUtil.extractRole(token);
				System.out.println("hello4");
				if(username!=null) {
					System.out.println("hello5");
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
					SecurityContextHolder.getContext().setAuthentication(auth);
					System.out.println("hello6");
				request.setAttribute("username", username);
				request.setAttribute("role", role);
				}
				System.out.println("hello7");
			} catch (Exception e) {
				System.out.println(e);
				System.out.println("hello8");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
				return;
			}
		
		
		System.out.println("hello9");
		System.out.println(request.getAttribute("username"));
		filterChain.doFilter(request, response);
	}
}

