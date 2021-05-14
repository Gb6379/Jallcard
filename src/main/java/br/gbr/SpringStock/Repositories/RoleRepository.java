package br.gbr.SpringStock.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gbr.SpringStock.Enums.Roles;
import br.gbr.SpringStock.Models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	public Optional<Role> findByName(Roles name);
}
