package br.gbr.SpringStock.Jwt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import br.gbr.SpringStock.Services.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component//custom beans
public class JwtUtils {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	
	@Value("${br.gbr.jwtSecret}")
	private String jwtSecret;
	
	@Value("${br.gbr.jwtExpirationMs}")
	private int jwtEpirationMs;
	
	public String generateJwtToken(Authentication auth) {
		
		Calendar cal = Calendar.getInstance(Locale.US);
        Calendar cal1 = Calendar.getInstance(Locale.US);
        cal1.setTime(cal.getTime());
        cal1.add(Calendar.SECOND, 300);
		CustomUserDetails userPrincipal = (CustomUserDetails) auth.getPrincipal();
		
		return Jwts.builder()
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date((new Date().getTime())))
				.setExpiration(new Date((new Date().getTime() + jwtEpirationMs)))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
	
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret)
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("String is empty {}", e.getMessage());
		}
		
		return false;
	}
}
