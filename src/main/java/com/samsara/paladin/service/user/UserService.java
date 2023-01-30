package com.samsara.paladin.service.user;

import java.util.List;

import com.samsara.paladin.dto.ResetPasswordDetails;
import com.samsara.paladin.dto.UserDto;

public interface UserService {

    UserDto registerUser(UserDto userDto);

    UserDto updateUser(UserDto userDto);

    UserDto assignAdminRoleToUser(String username);

    void deleteUser(String username);

    List<UserDto> loadAllUsers();

    UserDto loadUserByUsername(String username);

    UserDto loadUserByEmail(String email);

    List<UserDto> loadUsersByFirstName(String firstName);

    List<UserDto> loadUsersByLastName(String lastName);

    boolean resetUserPassword(ResetPasswordDetails resetPasswordDetails);
}
