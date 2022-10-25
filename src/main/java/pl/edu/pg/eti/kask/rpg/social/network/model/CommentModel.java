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

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CommentModel {

    private Integer id;

    private String content;

    public static Function<Comment, CommentModel> entityToModelMapper() {
        return comment -> CommentModel.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .build();
    }

}
