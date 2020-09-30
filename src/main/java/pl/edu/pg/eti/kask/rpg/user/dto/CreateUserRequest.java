package pl.edu.pg.eti.kask.rpg.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * PSOT user request. Contains only fields that can be set during user creation. User is defined in
 * {@link pl.edu.pg.eti.kask.rpg.user.entity.User}.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class CreateUserRequest {

    /**
     * User's login.
     */
    private String login;

    /**
     * User's name.
     */
    private String name;

    /**
     * User's surname.
     */
    private String surname;

    /**
     * User's birth date.
     */
    private LocalDate birthDate;

    /**
     * User's password.
     */
    private String password;

    /**
     * User's email.
     */
    private String email;

}
