package br.gbr.JallcardBackend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gbr.JallcardBackend.Models.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>{

}
