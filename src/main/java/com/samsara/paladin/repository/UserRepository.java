package com.samsara.paladin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsara.paladin.model.User;

@Repository
public interface UserRepository extends ListCrudRepository<User, Long> {

    @Query(
            "select u "
                    + "from User u "
                    + "join fetch u.roles "
                    + "where u.username = :username "
    )
    Optional<User> getUsersWithFetchedRoles(@Param("username") String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByFirstName(String firstName);

    List<User> findByLastName(String firstName);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
