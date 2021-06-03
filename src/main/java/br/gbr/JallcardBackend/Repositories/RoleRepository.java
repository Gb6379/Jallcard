package br.gbr.JallcardBackend.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gbr.JallcardBackend.Enums.Roles;
import br.gbr.JallcardBackend.Models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	public Optional<Role> findByName(Roles name);
}
