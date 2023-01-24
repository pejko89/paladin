package com.samsara.paladin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.samsara.paladin.model.Hero;
import com.samsara.paladin.model.User;

@Repository
public interface HeroRepository extends ListCrudRepository<Hero, Long> {

    List<Hero> findByType(String type);

    List<Hero> findByLevel(Integer level);

    List<Hero> findByUser(User user);

    List<Hero> findTop10ByOrderByCreatedDesc();

    Optional<Hero> findByName(String name);
}
