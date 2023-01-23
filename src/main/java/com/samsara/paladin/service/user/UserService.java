package com.samsara.paladin.service.user;

import java.util.List;

import com.samsara.paladin.dto.UserDto;
import com.samsara.paladin.enums.RoleEnum;

public interface UserService {

    UserDto registerUser(UserDto userDto, RoleEnum roleEnum);

    UserDto updateUser(UserDto userDto);

    void deleteUser(UserDto userDto);

    List<UserDto> loadAllUsers();

    UserDto loadUserByUsername(String username);

    UserDto loadUserByEmail(String email);

    List<UserDto> loadByFirstName(String firstName);

    List<UserDto> loadByLastName(String lastName);

    boolean resetPassword(String username, String secretAnswer, String newPassword);
}
