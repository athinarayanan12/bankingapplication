package com.security.bank.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	JwtAuthenticationHelper helper;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String header = request.getHeader("Authorization");
		String username=null,token=null;
		if(header !=null && header.startsWith("Bearer")) {
			token = header.substring(7);
			try {
				username =helper.getUsernameFromToken(token);
			
				if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null) {
					if(!helper.isTokenExpired(token)) {
						UserDetails userDetails= userDetailsService.loadUserByUsername(username);
						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =new UsernamePasswordAuthenticationToken(token,null, userDetails.getAuthorities());
						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		filterChain.doFilter(request, response);
	}

}
