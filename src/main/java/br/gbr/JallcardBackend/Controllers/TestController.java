package br.gbr.JallcardBackend.Controllers;

import javax.annotation.security.RolesAllowed;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/test")
public class TestController {

	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	
	@GetMapping("/user")
	//@PreAuthorize("hasRole('ROLE_USER')")
	public String userAccess() {
		return "User Content";
	}
	
	@GetMapping("/mod")
	//@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	public String modAccess() {
		return "User Content";
	}
	
	@GetMapping("/admin")
	//@PreAuthorize(" hasRole('ADMIN')")
	public String adminAccess() {
		return "User Content";
	}
	
	
}