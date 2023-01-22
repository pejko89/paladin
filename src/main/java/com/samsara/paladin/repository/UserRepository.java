package com.samsara.paladin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.samsara.paladin.model.user.User;

@Repository
public interface UserRepository extends ListCrudRepository<User, Long> {

    List<User> findByFirstName(String firstName);

    List<User> findByLastName(String lastName);

    Optional<User> findByEmail(String email);

//    List<User> findByRole(Role role);

    List<User> findByEnabled(boolean enabled);

    boolean existsByEmail(String email);

}
