package com.samsara.paladin.dto;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private static final int FIELD_MAX_SIZE = 60;

    private Long id;

    @NotEmpty
    @Size(max = FIELD_MAX_SIZE)
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9]{2,}$",
            message = "First name should have at least 2 characters and must not contain whitespace")
    private String firstName;

    @NotEmpty
    @Size(max = FIELD_MAX_SIZE)
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9]{2,}$",
            message = "Last name should have at least 2 characters and must not contain whitespace")
    private String lastName;

    @NotEmpty
    private String username;

    @NotEmpty
    @Size(max = FIELD_MAX_SIZE)
    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
            message = "password should have at least 8 characters,"
                    + "at least one uppercase letter,"
                    + "one lowercase letter"
                    + "and one number")
    private String password;

    private String about;

    private Integer heroCount;

    private Date createdAt;

    private Boolean enabled;

    @NotEmpty
    private String secretQuestion;

    @NotEmpty
    private String secretAnswer;
}
