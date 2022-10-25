package pl.edu.pg.eti.kask.rpg.social.network.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.edu.pg.eti.kask.rpg.social.network.entity.Comment;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;

import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CreateCommentModel {

    private String content;

    public static Function<CreateCommentModel, Comment> modelToEntityMapper(
            Supplier<User> userSupplier) {
        return model -> Comment.builder()
                .content(model.content)
                .createdById(userSupplier.get().getId())
                .build();
    }

}
