package com.samsara.paladin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

//    @Autowired
//    private UserServiceImpl userService;

//    @PostMapping("/register")
//    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
//        User user = convertToEntity(userDto);
//        user.setRoles(Role.USER);
//        UserDto userResponse = convertToDto(userService.createUser(user));
//        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
//    }
//
//    @PostMapping("/register/admin")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public UserDto registerAdmin(@Valid @RequestBody UserDto userDto) {
//        User user = convertToEntity(userDto);
//        user.setRole(Role.ADMIN);
//        User createdUser = userService.createUser(user);
//        return convertToDto(createdUser);
//    }
//
//    @PutMapping("/update")
//    public UserDto updateUser(@Valid @RequestBody UserDto userDto) {
//        User user = convertToEntity(userDto);
//        User createdUser = userService.updateUser(user);
//        return convertToDto(createdUser);
//    }

//    @GetMapping("/all")
//    public List<UserDto> returnAllUsers() {
//        return userService.loadAllUsers()
//                .stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }

//    private UserDto convertToDto(User user) {
//        return modelMapper.map(user, UserDto.class);
//    }
//
//    private User convertToEntity(UserDto userDto) {
//        User user = modelMapper.map(userDto, User.class);
//        if (userDto.getId() != null) {
//            User oldUser = userService.loadById(userDto.getId());
//            user.setId(oldUser.getId());
//        }
//        return user;
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleValidationExceptions(
//            MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return errors;
//    }

}
