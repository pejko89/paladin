package com.samsara.paladin.service.role;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsara.paladin.enums.RoleEnum;
import com.samsara.paladin.model.Role;
import com.samsara.paladin.repository.RoleRepository;

@Service
public class RoleServiceImpl {

    @Autowired
    private RoleRepository roleRepository;

    public Set<Role> setRoleUser() {
        Role role = roleRepository.findByName(RoleEnum.USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return roles;
    }

    public Set<Role> setRoleAdmin() {
        Role role = roleRepository.findByName(RoleEnum.ADMIN);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return roles;
    }
}
