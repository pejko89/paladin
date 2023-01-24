package com.samsara.paladin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsara.paladin.enums.RoleEnum;
import com.samsara.paladin.model.Role;

@Repository
public interface RoleRepository extends ListCrudRepository<Role, Long> {

    @Query(
            "select u.roles "
                    + "from User u "
                    + "where u.username = :username "
    )
    List<Role> getRolesByUser(@Param("username") String username);

    Role findByName(RoleEnum name);
}
