package com.samsara.paladin.service.user;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.samsara.paladin.dto.ResetPasswordDetails;
import com.samsara.paladin.dto.UserDto;
import com.samsara.paladin.enums.RoleName;
import com.samsara.paladin.exceptions.user.EmailExistsException;
import com.samsara.paladin.exceptions.user.EmailNotFoundException;
import com.samsara.paladin.exceptions.user.ResetPasswordFailedException;
import com.samsara.paladin.exceptions.user.UsernameExistsException;
import com.samsara.paladin.exceptions.user.UsernameNotFoundException;
import com.samsara.paladin.model.Role;
import com.samsara.paladin.model.User;
import com.samsara.paladin.repository.RoleRepository;
import com.samsara.paladin.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserServiceImpl.class})
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    @DisplayName("Register user test when user is valid then correct")
    void registerUserTestWhenUserIsValidThenCorrect() {

        UserDto userDto = UserDto.builder()
                .username("Test")
                .email("test@mai.com")
                .build();

        User user = User.builder()
                .username("test")
                .email("test@email.com")
                .password("testCryptPassword")
                .enabled(true)
                .creationDate(new Date())
                .build();

        Role role = Role.builder()
                .id(1L)
                .name(RoleName.USER)
                .build();

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        user.setRoles(roles);

        when(userRepository.existsByUsername(anyString()))
                .thenReturn(false);
        when(userRepository.existsByEmail(anyString()))
                .thenReturn(false);
        when(modelMapper.map(userDto, User.class))
                .thenReturn(user);
        when(passwordEncoder.encode(anyString()))
                .thenReturn("testCryptPassword");
        when(roleRepository.findByName(RoleName.USER))
                .thenReturn(role);
        when(userRepository.save(user))
                .thenReturn(user);
        when(modelMapper.map(user, UserDto.class))
                .thenReturn(userDto);

        UserDto responseUser = userService.registerUser(userDto);

        verify(userRepository).existsByUsername(anyString());
        verify(userRepository).existsByEmail(anyString());
        verify(modelMapper).map(userDto, User.class);
        verify(roleRepository).findByName(any());
        verify(passwordEncoder).encode(anyString());
        verify(userRepository).save(userArgumentCaptor.capture());
        verify(modelMapper).map(user, UserDto.class);
        User capturedUser = userArgumentCaptor.getValue();

        assertAll(
                "Grouped Assertions of User",
                () -> assertEquals(user.getUsername(), capturedUser.getUsername()),
                () -> assertEquals(user.getEmail(), capturedUser.getEmail()),
                () -> assertEquals(user.getPassword(), capturedUser.getPassword()),
                () -> assertEquals(user.getRoles(), capturedUser.getRoles()),
                () -> assertTrue(capturedUser.getEnabled()),
                () -> assertNotNull(capturedUser.getCreationDate()),
                () -> assertNotNull(responseUser)
        );
    }

    @Test
    @DisplayName("Register user test when username exists then throw")
    void registerUserTestWhenUsernameExistsThenThrow() {

        UserDto userDto = UserDto.builder()
                .username("Test")
                .build();

        when(userRepository.existsByUsername(userDto.getUsername()))
                .thenReturn(true);

        assertThrows(UsernameExistsException.class, () -> {
            userService.registerUser(userDto);
            verify(userRepository).existsByUsername(any());
            verify(userRepository, never()).save(any());
        });
    }

    @Test
    @DisplayName("Register user test when email exists then throw")
    void registerUserTestWhenEmailExistsThenThrow() {

        UserDto userDto = UserDto.builder()
                .username("Test")
                .email("test@mai.com")
                .build();

        when(userRepository.existsByUsername(anyString()))
                .thenReturn(false);
        when(userRepository.existsByEmail(userDto.getEmail()))
                .thenReturn(true);

        assertThrows(EmailExistsException.class, () -> {
            userService.registerUser(userDto);
            verify(userRepository).existsByUsername(userDto.getUsername());
            verify(userRepository).existsByEmail(userDto.getEmail());
            verify(userRepository, never()).save(any());
        });
    }

    @Test
    @DisplayName("Update user test when user is valid then correct")
    void updateUserTestWhenUserIsValidThenCorrect() {

        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("test")
                .password("Test123")
                .build();

        User user = User.builder()
                .id(1L)
                .username("test")
                .email("test@email.com")
                .password("testCryptPassword")
                .build();

        when(userRepository.findByUsername(userDto.getUsername()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.encode(user.getPassword()))
                .thenReturn("testCryptPassword");
        when(userRepository.save(user))
                .thenReturn(user);
        when(modelMapper.map(user, UserDto.class))
                .thenReturn(userDto);

        UserDto updatedUser = userService.updateUser(userDto);

        verify(userRepository).findByUsername(userDto.getUsername());
        verify(modelMapper).map(userDto, user);
        verify(passwordEncoder).encode(user.getPassword());
        verify(userRepository).save(userArgumentCaptor.capture());
        verify(modelMapper).map(user, UserDto.class);
        User capturedUser = userArgumentCaptor.getValue();

        assertAll(
                "Grouped Assertions of User",
                () -> assertEquals(user.getUsername(), capturedUser.getUsername()),
                () -> assertEquals(user.getEmail(), capturedUser.getEmail()),
                () -> assertEquals(user.getPassword(), capturedUser.getPassword()),
                () -> assertNotNull(updatedUser)
        );
    }

    @Test
    @DisplayName("Update user test when user not found then throw")
    void updateUserTestWhenUserNotFoundThenThrow() {

        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("test")
                .build();

        when(userRepository.findByUsername(userDto.getUsername()))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.updateUser(userDto);
            verify(userRepository).findByUsername(userDto.getUsername());
        });
    }

    @Test
    @DisplayName("Update user test when user dto password is null then correct")
    void updateUserTestWhenUserDtoPasswordIsNullThenCorrect() {

        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("test")
                .build();

        User user = User.builder()
                .id(1L)
                .username("test")
                .email("test@email.com")
                .build();

        when(userRepository.findByUsername(userDto.getUsername()))
                .thenReturn(Optional.of(user));
        when(userRepository.save(user))
                .thenReturn(user);
        when(modelMapper.map(user, UserDto.class))
                .thenReturn(userDto);

        UserDto updatedUser = userService.updateUser(userDto);

        verify(userRepository).findByUsername(userDto.getUsername());
        verify(modelMapper).map(userDto, user);
        verify(userRepository).save(user);
        verify(modelMapper).map(user, UserDto.class);

        verify(passwordEncoder, never()).encode(anyString());

        assertNotNull(updatedUser, "Updated user should not be null");
    }

    @Test
    @DisplayName("Assign admin role to user test when user found then correct")
    void assignAdminRoleToUserTestWhenUserFoundThenCorrect() {
        UserDto userDto = UserDto.builder()
                .username("test")
                .build();

        User user = User.builder()
                .username("test")
                .build();

        Role roleUser = Role.builder()
                .id(1L)
                .name(RoleName.USER)
                .build();

        Role roleAdmin = Role.builder()
                .id(2L)
                .name(RoleName.ADMIN)
                .build();

        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);

        user.setRoles(roles);

        Set<Role> expectedRoles = new HashSet<>();
        expectedRoles.add(roleUser);
        expectedRoles.add(roleAdmin);

        when(userRepository.findByUsername(userDto.getUsername()))
                .thenReturn(Optional.of(user));
        when(roleRepository.findByName(RoleName.ADMIN))
                .thenReturn(roleAdmin);
        when(userRepository.save(user))
                .thenReturn(user);
        when(modelMapper.map(user, UserDto.class))
                .thenReturn(userDto);

        UserDto updatedUserDto = userService.assignAdminRoleToUser(userDto.getUsername());

        verify(userRepository).findByUsername(userDto.getUsername());
        verify(roleRepository).findByName(RoleName.ADMIN);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        verify(modelMapper).map(user, UserDto.class);

        assertEquals(expectedRoles, capturedUser.getRoles());
        assertNotNull(updatedUserDto);
    }

    @Test
    @DisplayName("Assign admin role to user test when user not found then throw")
    void assignAdminRoleToUserTestWhenUserNotFoundThenThrow() {
        UserDto userDto = UserDto.builder()
                .username("test")
                .build();

        when(userRepository.findByUsername(userDto.getUsername()))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.assignAdminRoleToUser(userDto.getUsername());
            verify(userRepository).findByUsername(userDto.getUsername());
            verify(userRepository, never()).save(any());
        });
    }

    @Test
    @DisplayName("Delete user test when user if found then correct")
    void deleteUserTestWhenUserIsFoundThenCorrect() {

        User user = User.builder()
                .username("test")
                .build();

        when(userRepository.findByUsername("test"))
                .thenReturn(Optional.of(user));

        userService.deleteUser("test");

        verify(userRepository).findByUsername("test");
        verify(userRepository).delete(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertEquals(user.getUsername(), capturedUser.getUsername());
    }

    @Test
    @DisplayName("Delete user test when user if not found then throw")
    void deleteUserTestWhenUserIsNotFoundThenThrow() {

        when(userRepository.findByUsername("test"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.deleteUser("test");
            verify(userRepository).findByUsername("test");
            verify(userRepository, never()).delete(any());
        });
    }

    @Test
    @DisplayName("Load all users test when multiple users found")
    void loadAllUsersTestWhenMultipleUsersFound() {
        UserDto userDto1 = UserDto.builder()
                .id(1L)
                .username("Test1")
                .build();
        UserDto userDto2 = UserDto.builder()
                .id(2L)
                .username("Test2")
                .build();
        List<UserDto> expectedResponseList = new ArrayList<>(Arrays.asList(userDto1, userDto2));

        User user1 = User.builder()
                .id(1L)
                .username("Test1")
                .build();
        User user2 = User.builder()
                .id(2L)
                .username("Test2")
                .build();
        List<User> usersInDb = new ArrayList<>(Arrays.asList(user1, user2));

        when(userRepository.findAll())
                .thenReturn(usersInDb);
        when(modelMapper.map(user1, UserDto.class))
                .thenReturn(userDto1);
        when(modelMapper.map(user2, UserDto.class))
                .thenReturn(userDto2);

        List<UserDto> responseList = userService.loadAllUsers();

        verify(userRepository).findAll();
        verify(modelMapper).map(user1, UserDto.class);
        verify(modelMapper).map(user2, UserDto.class);

        assertEquals(expectedResponseList.size(), responseList.size());
    }

    @Test
    @DisplayName("Load all users test when no users are stored")
    void loadAllUsersTestWhenNoUsersAreStored() {

        when(userRepository.findAll())
                .thenReturn(new ArrayList<>());

        List<UserDto> responseList = userService.loadAllUsers();

        verify(userRepository).findAll();
        verify(modelMapper, never()).map(any(), any());

        assertEquals(0, responseList.size());
    }

    @Test
    @DisplayName("Load user by username test when username found then correct")
    void loadUserByUsernameTestWhenUsernameFoundThenCorrect() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("Test")
                .build();

        User user = User.builder()
                .id(1L)
                .username("Test")
                .build();

        when(userRepository.findByUsername("Test"))
                .thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class))
                .thenReturn(userDto);

        UserDto response = userService.loadUserByUsername("Test");

        verify(userRepository).findByUsername("Test");
        verify(modelMapper).map(user, UserDto.class);

        assertEquals("Test", response.getUsername());
    }

    @Test
    @DisplayName("Load user by username test when username not found then throw")
    void loadUserByUsernameTestWhenUsernameNotFoundThenThrow() {

        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("Test");
            verify(userRepository).findByUsername("Test");
            verify(modelMapper, never()).map(any(), any());
        });
    }

    @Test
    @DisplayName("Load user by email test when email found then correct")
    void loadUserByEmailTestWhenEmailFoundThenCorrect() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("Test")
                .build();

        User user = User.builder()
                .id(1L)
                .email("Test")
                .build();

        when(userRepository.findByEmail("Test"))
                .thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class))
                .thenReturn(userDto);

        UserDto response = userService.loadUserByEmail("Test");

        verify(userRepository).findByEmail("Test");
        verify(modelMapper).map(user, UserDto.class);

        assertEquals("Test", response.getEmail());
    }

    @Test
    @DisplayName("Load user by email test when email not found then throw")
    void loadUserByEmailTestWhenEmailNotFoundThenThrow() {

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(EmailNotFoundException.class, () -> {
            userService.loadUserByEmail("Test");
            verify(userRepository).findByEmail("Test");
            verify(modelMapper, never()).map(any(), any());
        });
    }

    @Test
    @DisplayName("Load users by first name test when multiple users found")
    void loadUsersByFirstNameTestWhenMultipleUsersFound() {
        UserDto userDto1 = UserDto.builder()
                .id(1L)
                .firstName("Test")
                .build();
        UserDto userDto2 = UserDto.builder()
                .id(2L)
                .firstName("Test")
                .build();
        List<UserDto> expectedResponseList = new ArrayList<>(Arrays.asList(userDto1, userDto2));

        User user1 = User.builder()
                .id(1L)
                .firstName("Test")
                .build();
        User user2 = User.builder()
                .id(2L)
                .firstName("Test")
                .build();
        List<User> usersInDb = new ArrayList<>(Arrays.asList(user1, user2));

        when(userRepository.findByFirstName("Test"))
                .thenReturn(usersInDb);
        when(modelMapper.map(user1, UserDto.class))
                .thenReturn(userDto1);
        when(modelMapper.map(user2, UserDto.class))
                .thenReturn(userDto2);

        List<UserDto> responseList = userService.loadUsersByFirstName("Test");

        verify(userRepository).findByFirstName("Test");
        verify(modelMapper).map(user1, UserDto.class);
        verify(modelMapper).map(user2, UserDto.class);

        assertEquals(expectedResponseList.size(), responseList.size());
    }

    @Test
    @DisplayName("Load users by first name test when no users are stored")
    void loadUsersByFirstNameTestWhenNoUsersAreStored() {

        when(userRepository.findByFirstName(anyString()))
                .thenReturn(new ArrayList<>());

        List<UserDto> responseList = userService.loadUsersByFirstName(anyString());

        verify(userRepository).findByFirstName(anyString());
        verify(modelMapper, never()).map(any(), any());

        assertEquals(0, responseList.size());
    }

    @Test
    @DisplayName("Load users by last name test when multiple users found")
    void loadUsersByLastNameTestWhenMultipleUsersFound() {
        UserDto userDto1 = UserDto.builder()
                .id(1L)
                .lastName("Test")
                .build();
        UserDto userDto2 = UserDto.builder()
                .id(2L)
                .lastName("Test")
                .build();
        List<UserDto> expectedResponseList = new ArrayList<>(Arrays.asList(userDto1, userDto2));

        User user1 = User.builder()
                .id(1L)
                .lastName("Test")
                .build();
        User user2 = User.builder()
                .id(2L)
                .lastName("Test")
                .build();
        List<User> usersInDb = new ArrayList<>(Arrays.asList(user1, user2));

        when(userRepository.findByLastName("Test"))
                .thenReturn(usersInDb);
        when(modelMapper.map(user1, UserDto.class))
                .thenReturn(userDto1);
        when(modelMapper.map(user2, UserDto.class))
                .thenReturn(userDto2);

        List<UserDto> responseList = userService.loadUsersByLastName("Test");

        verify(userRepository).findByLastName("Test");
        verify(modelMapper).map(user1, UserDto.class);
        verify(modelMapper).map(user2, UserDto.class);

        assertEquals(expectedResponseList.size(), responseList.size());
    }

    @Test
    @DisplayName("Load users by last name test when no users are stored")
    void loadUsersByLastNameTestWhenNoUsersAreStored() {

        when(userRepository.findByLastName(anyString()))
                .thenReturn(new ArrayList<>());

        List<UserDto> responseList = userService.loadUsersByLastName(anyString());

        verify(userRepository).findByLastName(anyString());
        verify(modelMapper, never()).map(any(), any());

        assertEquals(0, responseList.size());
    }

    @Test
    @DisplayName("Reset password test when all is valid then correct")
    void resetUserPasswordTestWhenAllIsValidThenCorrect() {

        ResetPasswordDetails resetPasswordDetails = ResetPasswordDetails.builder()
                .username("Test")
                .secretAnswer("testSecretAnswer")
                .newPassword("newPassword")
                .build();

        User storedUser = User.builder()
                .id(1L)
                .username("Test")
                .password("encodedPassword")
                .secretAnswer("testSecretAnswer")
                .build();

        when(userRepository.findByUsername("Test"))
                .thenReturn(Optional.of(storedUser));
        when(passwordEncoder.encode("newPassword"))
                .thenReturn("encodedPassword");
        when(userRepository.save(storedUser))
                .thenReturn(storedUser);

        boolean response = userService.resetUserPassword(resetPasswordDetails);

        verify(userRepository).findByUsername("Test");
        verify(passwordEncoder).encode("newPassword");
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertAll(
                "Grouped Assertions of password",
                () -> assertEquals("encodedPassword", capturedUser.getPassword()),
                () -> assertTrue(response)
        );
    }

    @Test
    @DisplayName("Reset password test when secret answer is invalid then throw")
    void resetUserPasswordTestWhenSecretAnswerIsInvalidThenThrow() {

        ResetPasswordDetails resetPasswordDetails = ResetPasswordDetails.builder()
                .username("Test")
                .secretAnswer("wrongSecretAnswer")
                .newPassword("newPassword")
                .build();

        User storedUser = User.builder()
                .id(1L)
                .username("Test")
                .password("encodedPassword")
                .secretAnswer("testSecretAnswer")
                .build();

        when(userRepository.findByUsername("Test"))
                .thenReturn(Optional.of(storedUser));

        assertThrows(ResetPasswordFailedException.class, () -> {
            userService.resetUserPassword(resetPasswordDetails);
            verify(passwordEncoder, never()).encode(anyString());
            verify(userRepository, never()).save(any());
        });
    }
}
