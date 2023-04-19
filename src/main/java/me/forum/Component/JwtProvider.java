package me.forum.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import me.forum.Dao.UserDao;

@Component
public class JwtProvider {
	private static JwtProvider instance;
	private final String secret = ".. .-.. --- ...- . -.-- --- ..-";
	private final long expired = 3 * 24 * 60 * 60 * 1000;
	@Autowired
	UserDao userDao;

	public JwtProvider() {
	}

	public String generate(String username) {
		String token = Jwts.builder().setClaims(new HashMap<String, Object>()).setSubject(username)
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expired))
				.signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256).compact();
		return token;
	}

	public boolean validateToken(String token) {
		return !isTokenExpired(token);
	}

	public boolean isTokenExpired(String token) {
		try {
			return extractExpiration(token).before(new Date());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return true;
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(secret.getBytes()).build().parseClaimsJws(token).getBody();
	}

	public static JwtProvider GetInstance() {
		if (instance == null) {
			instance = new JwtProvider();
		}
		return instance;
	}
}
