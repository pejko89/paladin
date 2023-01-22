package com.samsara.paladin.service.user;

import java.util.Collections;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.samsara.paladin.exceptions.user.EmailExistsException;
import com.samsara.paladin.model.user.RoleEnum;
import com.samsara.paladin.model.user.User;
import com.samsara.paladin.model.user.UserDto;
import com.samsara.paladin.repository.RoleRepository;
import com.samsara.paladin.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public UserDto registerNewUserAccount(UserDto accountDto) throws EmailExistsException {
        if (emailExist(accountDto.getEmail())) {
            throw new EmailExistsException
                    ("There is an account with that email address: " + accountDto.getEmail());
        }
        User user = convertToEntity(accountDto);
        encryptUserPassword(user);
        assignRolesToUser(user, RoleEnum.USER.name());
        return convertToDto(saveUser(user));
    }

    private void encryptUserPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    private void assignRolesToUser(User user, String role) {
        user.setRoles(Collections.singletonList(roleRepository.findByName(role)));
    }

    private boolean emailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    private User saveUser(User user) {
        return userRepository.save(user);
    }

    private User convertToEntity(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        if (userDto.getId() != null) {
            User oldUser = userRepository.findById(userDto.getId()).orElseThrow();
            user.setId(oldUser.getId());
        }
        return user;
    }

    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
