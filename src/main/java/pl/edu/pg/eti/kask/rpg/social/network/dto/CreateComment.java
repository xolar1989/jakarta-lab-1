package pl.edu.pg.eti.kask.rpg.social.network.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pl.edu.pg.eti.kask.rpg.social.network.entity.Comment;
import pl.edu.pg.eti.kask.rpg.social.network.entity.CommentType;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;

import java.io.Serializable;
import java.util.function.BiFunction;
import java.util.function.Function;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CreateComment implements Serializable {

    private String content;

    private String type;

    public static BiFunction<CreateComment, User, Comment> dtoToEntityMapper() {
        return (request, user) -> Comment.builder()
                .content(request.getContent())
                .user(user)
                .type(CommentType.fromString(request.getType()))
                .build();
    }
}
