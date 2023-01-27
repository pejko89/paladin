package com.samsara.paladin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsara.paladin.enums.PermissionEnum;
import com.samsara.paladin.enums.RoleEnum;
import com.samsara.paladin.model.Permission;

@Repository
public interface PermissionRepository extends ListCrudRepository<Permission, Long> {

    @Query(
            "select r.permissions "
                    + "from Role r "
                    + "where r.name = :name "
    )
    List<Permission> getPermissionsByRole(@Param("name") RoleEnum name);

    Permission findByName(PermissionEnum name);
}
