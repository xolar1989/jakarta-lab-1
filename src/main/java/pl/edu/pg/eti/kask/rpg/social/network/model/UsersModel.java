package pl.edu.pg.eti.kask.rpg.social.network.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UsersModel implements Serializable {


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class UserModel {

        private Integer id;

        /**
         * User's login.
         */
        private String login;

        /**
         * Users's given name.
         */
        private String name;

    }

    @Singular
    public List<UserModel> users;

    public static Function<Collection<User>, UsersModel> entityToModelMapper() {
        return userCollection -> {
            UsersModel.UsersModelBuilder model = UsersModel.builder();
            userCollection.stream()
                    .map(user -> UsersModel.UserModel.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .login(user.getLogin())
                            .build())
                    .forEach(model::user);
            return model.build();
        };
    }

}
