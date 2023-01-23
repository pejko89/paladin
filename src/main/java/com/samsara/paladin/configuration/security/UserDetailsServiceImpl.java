package com.samsara.paladin.configuration.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.samsara.paladin.model.Permission;
import com.samsara.paladin.model.Role;
import com.samsara.paladin.model.User;
import com.samsara.paladin.repository.RoleRepository;
import com.samsara.paladin.repository.UserRepository;

//@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("username not found with email " + username);
        } else {
            return composeUserDetails(user.get());
        }
    }

    private org.springframework.security.core.userdetails.User composeUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                getAuthorities(user.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {
        return getGrantedAuthorities(getPermissions(roles));
    }

    private List<String> getPermissions(Collection<Role> roles) {
        List<String> grantedAuthorities = new ArrayList<>();
        List<Permission> permissions = new ArrayList<>();
        for (Role role : roles) {
            grantedAuthorities.add(role.getName().getAuthority());
            permissions.addAll(role.getPermissions());
        }
        for (Permission permission : permissions) {
            grantedAuthorities.add(permission.getName().getAuthority());
        }
        return grantedAuthorities;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> permissions) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return authorities;
    }
}
