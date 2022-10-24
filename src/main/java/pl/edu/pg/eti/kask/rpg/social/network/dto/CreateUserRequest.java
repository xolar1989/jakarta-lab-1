package pl.edu.pg.eti.kask.rpg.social.network.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;

import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CreateUserRequest {
    /**
     * User's login.
     */
    private String login;

    /**
     * Users's given name.
     */
    private String name;

    /**
     * User's password.
     */
    @ToString.Exclude
    private String password;

    /**
     * User's contact email.
     */
    private String email;

    public static Function<CreateUserRequest, User> dtoToEntityMapper() {
        return request -> User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .login(request.getLogin())
                .password(request.getPassword())
                .build();
    }

}
