package br.gbr.JallcardBackend.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gbr.JallcardBackend.Models.Contact;
import br.gbr.JallcardBackend.Repositories.ContactRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/contacts")
public class ContactController {
	
	@Autowired
	private ContactRepository contactRepository;
	
	@GetMapping
	public List<Contact> getContacts(){
		return contactRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public Optional<Contact> getContactById(@PathVariable Long id) {
		return contactRepository.findById(id);
	}
	
	@PostMapping("/save")
	public Contact saveContact(@RequestBody Contact contact) {
		return contactRepository.save(contact);
	}
	
	@PutMapping("/{id}")
	public Contact updateContact(@PathVariable Long id, @RequestBody Contact contact) {
		
		return contactRepository.findById(id)
			      .map(record -> {
			        record.setName(contact.getName());
			        record.setLastName(contact.getLastName());
			        record.setKinship(contact.getKinship());
			        record.setBirthDate(contact.getBirthDate());
			        record.setPhones(contact.getPhones());
			        return contactRepository.save(record);
			      })
			      .orElseGet(() -> {
			        return contactRepository.save(contact);
			      });
	}
	
	
	@DeleteMapping("/{id}")
	public void deleteContact(@PathVariable Long id) {
		contactRepository.deleteById(id);
	}
	
	

}
