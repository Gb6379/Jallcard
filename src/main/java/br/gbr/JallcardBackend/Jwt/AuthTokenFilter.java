package br.gbr.JallcardBackend.Jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.jaas.JaasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import br.gbr.JallcardBackend.Services.CustomUserDetailsService;
import io.jsonwebtoken.JwtException;

public class AuthTokenFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		 try {
			 String jwt = getJwt(request);
			 if (jwt != null) {
				 		 //getusername as of token given
						 String username = jwtUtils.getUserNameFromJwtToken(jwt);
						 //System.out.println("username" + username);
						 //user details core
						 UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
						 //auth pass 
						 UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								 userDetails, null, userDetails.getAuthorities()
								 );
						 authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						 
						 SecurityContextHolder.getContext().setAuthentication(authentication);
						 
						 filterChain.doFilter(request, response);
			 		}
			  
			 } catch (Exception e) {
				 logger.error("Cannot set user authentication: {}", e);
			 }
		 
		 filterChain.doFilter(request, response);
		
	}

	private String getJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth == null || !headerAuth.startsWith("Bearer")) {
        	throw new JwtException("No JWT token found in request headers");
           // return headerAuth.substring(7, headerAuth.length());
        }
        
        return headerAuth.replace("Bearer ", "");
    }

}
