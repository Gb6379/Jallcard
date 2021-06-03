package br.gbr.JallcardBackend.Controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gbr.JallcardBackend.Enums.Roles;
import br.gbr.JallcardBackend.Jwt.JwtUtils;
import br.gbr.JallcardBackend.Models.Role;
import br.gbr.JallcardBackend.Models.User;
import br.gbr.JallcardBackend.Repositories.RoleRepository;
import br.gbr.JallcardBackend.Repositories.UserRepository;
import br.gbr.JallcardBackend.Request.LoginRequest;
import br.gbr.JallcardBackend.Request.SignupRequest;
import br.gbr.JallcardBackend.Response.JwtResponse;
import br.gbr.JallcardBackend.Response.MessageResponse;
import br.gbr.JallcardBackend.Services.CustomUserDetails;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@PostMapping("/signIn")//login
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){//response entity is a status response from jwt
		
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();
		 if (username == null) throw new UsernameNotFoundException("username not found!");
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						username,
						password
						)
				);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = jwtUtils.generateJwtToken(authentication);//generate jwt token bearer
		
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		
		//get user's roles list
		List<String> roles = customUserDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
		
		return ResponseEntity.ok(new JwtResponse(jwt,"Bearer", customUserDetails.getId()
				,customUserDetails.getUsername(), roles));
		
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
		if(userRepository.existsByUsername(signUpRequest.getUsername())) {//don't acept same username
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username already in use"));
		}
		
		//new User acc
		System.out.println("" + signUpRequest.getPassword());
		System.out.println("" + signUpRequest.getUsername());
		User user = new User(signUpRequest.getUsername(), 
				encoder.encode(signUpRequest.getPassword()
				));
		
		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();
		
		if (strRoles == null) {
			Role userRole = roleRepository.findByName(Roles.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch(role) {
			/*	case "admin":
						Role adminRole = roleRepository.findByName(Roles.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: role is not foun"));
						roles.add(adminRole);
						
						break;
				case "mod":
					Role modRole = roleRepository.findByName(Roles.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: role is not found."));
					roles.add(modRole);
					
					break;*/
					
				default: 
					Role userRole = roleRepository.findByName(Roles.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
			
		}
		
		user.setRoles(roles);
		userRepository.save(user);
		
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
		
		
	}

}
