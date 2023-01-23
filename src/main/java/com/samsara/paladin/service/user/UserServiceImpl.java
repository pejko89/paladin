package com.samsara.paladin.service.user;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.samsara.paladin.dto.UserDto;
import com.samsara.paladin.enums.RoleEnum;
import com.samsara.paladin.exceptions.user.EmailExistsException;
import com.samsara.paladin.exceptions.user.UserNotFoundException;
import com.samsara.paladin.exceptions.user.UsernameExistsException;
import com.samsara.paladin.model.User;
import com.samsara.paladin.repository.RoleRepository;
import com.samsara.paladin.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public UserDto registerUser(UserDto accountDto, RoleEnum roleEnum) throws EmailExistsException {
        if (usernameExist(accountDto.getUsername())) {
            throw new UsernameExistsException("There is an account with that username: " + accountDto.getUsername());
        }
        if (emailExist(accountDto.getEmail())) {
            throw new EmailExistsException("There is an account with that email address: " + accountDto.getEmail());
        }
        User user = convertToEntity(accountDto);
        encryptUserPassword(user);
        assignRolesToUser(user, roleEnum.name());
        return convertToDto(saveUser(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User storedUser = modelMapper.map(userDto, User.class);
        return convertToDto(saveUser(storedUser));
    }

    @Override
    public void deleteUser(UserDto userDto) {
        if (!usernameExist(userDto.getUsername())) {
            throw new UsernameNotFoundException("User '" + userDto.getUsername() + "' doesn't exist!");
        }
        userRepository.delete(convertToEntity(userDto));
    }

    @Override
    public List<UserDto> loadAllUsers() {
        return convertToDtoList(userRepository.findAll());
    }

    @Override
    public UserDto loadUserByUsername(String username) {
        String errorMessage = String.format("User '%s' doesn't exist!", username);
        return userRepository.findByUsername(username)
                .map(this::convertToDto)
                .orElseThrow(
                        () -> new UserNotFoundException(errorMessage)
                );

    }

    @Override
    public UserDto loadUserByEmail(String email) {
        String errorMessage = String.format("User with email '%s' doesn't exist!", email);
        return userRepository.findByEmail(email)
                .map(this::convertToDto)
                .orElseThrow(
                        () -> new UserNotFoundException(errorMessage)
                );
    }

    @Override
    public List<UserDto> loadByFirstName(String firstName) {
        return convertToDtoList(userRepository.findByFirstName(firstName));
    }

    @Override
    public List<UserDto> loadByLastName(String lastName) {
        return convertToDtoList(userRepository.findByLastName(lastName));
    }

    @Override
    public boolean resetPassword(String username, String secretAnswer, String newPassword) {
        User user;
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return false;
        } else if (!optionalUser.get().getSecretAnswer().equals(secretAnswer)) {
            return false;
        } else {
            user = optionalUser.get();
        }
        encryptUserPassword(user);
        saveUser(user);
        return true;
    }

    private boolean usernameExist(String username) {
        return userRepository.existsByUsername(username);
    }

    private boolean emailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    private void encryptUserPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    private void assignRolesToUser(User user, String role) {
        user.setRoles(Collections.singletonList(roleRepository.findByName(role)));
    }

    private User saveUser(User user) {
        return userRepository.save(user);
    }

    private List<UserDto> convertToDtoList(List<User> users) {
        return users
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User convertToEntity(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        if (userDto.getId() != null) {
            User oldUser = userRepository.findById(userDto.getId()).orElseThrow();
            user.setId(oldUser.getId());
        }
        return user;
    }
}
