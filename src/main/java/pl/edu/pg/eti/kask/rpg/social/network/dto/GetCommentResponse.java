package pl.edu.pg.eti.kask.rpg.social.network.dto;

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

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.function.Function;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class GetCommentResponse {

    private Integer id;

    private String content;

    private GetUserResponse user;

    private String type;

    public static Function<Comment, GetCommentResponse> entityToDtoMapper() {
        return comment -> GetCommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(GetUserResponse.builder()
                        .name(comment.getUser().getName())
                        .email(comment.getUser().getEmail())
                        .login(comment.getUser().getLogin())
                        .build())
                .type(comment.getType() != null ? comment.getType().getType() : null)
                .build();
    }
}
