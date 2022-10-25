package pl.edu.pg.eti.kask.rpg.social.network.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.edu.pg.eti.kask.rpg.social.network.entity.Comment;
import pl.edu.pg.eti.kask.rpg.social.network.entity.CommentType;
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
public class UpdateCommentModel {

    private String content;

    private CommentTypeModel type;

    public static Function<UpdateCommentModel, Comment> modelToEntityMapper(
            Supplier<Integer> userSupplier) {
        return model -> Comment.builder()
                .content(model.content)
                .createdById(userSupplier.get())
                .type(CommentType.fromString(model.type.getType()))
                .build();
    }
}
