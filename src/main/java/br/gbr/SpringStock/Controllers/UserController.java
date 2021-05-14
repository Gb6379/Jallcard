package br.gbr.SpringStock.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gbr.SpringStock.Models.User;
import br.gbr.SpringStock.Repositories.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping
	public User saveUser(@RequestBody User user) {
		return userRepository.save(user);
	}
	
	@GetMapping
	public List<User> getAll() {
		return userRepository.findAll();
	}
	
	@PutMapping("/{id}")
	public User updateUser(@RequestBody User user, @PathVariable Long id) {
		
		User u = userRepository.getOne(id);
		
		if (u == null) {
			return null;
		}
		
		u.setUsername(user.getUsername());
		u.setRoles(user.getRoles());
		
		return userRepository.save(u);
	}
	
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Long id) {
		userRepository.deleteById(null);
	}

}
