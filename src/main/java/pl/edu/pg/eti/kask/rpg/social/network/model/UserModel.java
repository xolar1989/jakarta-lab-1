package pl.edu.pg.eti.kask.rpg.social.network.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;

import java.util.Collection;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UserModel {


    private Integer id;

    /**
     * User's login.
     */
    private String login;

    /**
     * Users's given name.
     */
    private String name;


    private String image;

    public static Function<User, UserModel> entityToModelMapper() {
        return user -> UserModel.builder()
                .id(user.getId())
                .login(user.getLogin())
                .name(user.getName())
                .image("/api/images/" + user.getId())
                .build();
    }

}
