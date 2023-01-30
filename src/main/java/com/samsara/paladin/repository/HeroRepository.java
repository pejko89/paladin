package com.samsara.paladin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.samsara.paladin.enums.HeroType;
import com.samsara.paladin.model.Hero;
import com.samsara.paladin.model.User;

@Repository
public interface HeroRepository extends ListCrudRepository<Hero, Long> {

    Optional<Hero> findByName(String name);

    List<Hero> findByUser(User user);

    List<Hero> findByType(HeroType type);

    List<Hero> findByLevel(Integer level);

    @Query(
            "SELECT h "
                    + "FROM Hero h "
                    + "WHERE h.level >= :level "
    )
    List<Hero> findByMinLevel(Integer level);

    @Query(
            "SELECT h "
                    + "FROM Hero h "
                    + "WHERE h.level <= :level "
    )
    List<Hero> findByMaxLevel(Integer level);

    @Query(
            "SELECT h "
                    + "FROM Hero h "
                    + "ORDER BY h.level DESC "
                    + "LIMIT :numberOfHeroes "
    )
    List<Hero> findBestByLevel(Integer numberOfHeroes);

    @Query(
            "SELECT h "
                    + "FROM Hero h "
                    + "ORDER BY h.creationDate DESC "
                    + "LIMIT :numberOfHeroes "
    )
    List<Hero> findLastAdded(Integer numberOfHeroes);

    boolean existsByName(String name);
}
