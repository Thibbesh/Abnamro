package nl.abnamro.com.recipes.repository;

import nl.abnamro.com.recipes.model.auth.ERole;
import nl.abnamro.com.recipes.model.auth.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
}
