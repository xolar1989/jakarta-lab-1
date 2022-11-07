package pl.edu.pg.eti.kask.rpg.social.network.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;
import pl.edu.pg.eti.kask.rpg.social.network.model.UsersModel;

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
public class GetUsersResponse implements Serializable {

    @Singular
    public List<GetUserResponse> users;

    public static Function<Collection<User>, GetUsersResponse> entityToDtoMapper() {
        return userCollection -> {
            GetUsersResponse.GetUsersResponseBuilder model = GetUsersResponse.builder();
            userCollection.stream()
                    .map(user -> GetUserResponse.builder()
                            .name(user.getName())
                            .login(user.getLogin())
                            .email(user.getEmail())
                            .build()
                    )
                    .forEach(model::user);
            return model.build();
        };
    }
}
