package com.abnamro.nl.recipes.repository;

import com.abnamro.nl.recipes.model.auth.ERole;
import com.abnamro.nl.recipes.model.auth.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
}
