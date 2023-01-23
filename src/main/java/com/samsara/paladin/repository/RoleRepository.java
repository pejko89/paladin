package com.samsara.paladin.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.samsara.paladin.model.Role;

@Repository
public interface RoleRepository extends ListCrudRepository<Role, Long> {

    Role findByName(String name);

}
