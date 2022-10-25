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
import pl.edu.pg.eti.kask.rpg.social.network.entity.Comment;
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
public class UserCommentsModel implements Serializable {

    @Singular
    public List<CommentModel> comments;

    public static Function<Collection<Comment>, UserCommentsModel> entityToModelMapper() {
        return commentCollection -> {
            UserCommentsModel.UserCommentsModelBuilder model = UserCommentsModel.builder();
             commentCollection.stream()
                    .map(comment -> CommentModel.builder()
                            .content(comment.getContent())
                            .id(comment.getId())
                            .build()
                    )
                    .forEach(model::comment);
            return model.build();
        };
    }

}
