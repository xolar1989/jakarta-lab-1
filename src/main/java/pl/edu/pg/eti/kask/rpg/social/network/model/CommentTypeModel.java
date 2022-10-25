package pl.edu.pg.eti.kask.rpg.social.network.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.edu.pg.eti.kask.rpg.social.network.entity.CommentType;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CommentTypeModel {

    private String type;

    public static Function<CommentType, CommentTypeModel> entityToModelMapper() {;
        return commentType -> {
            System.out.println(commentType);
            return CommentTypeModel.builder()
                    .type(commentType.getType())
                    .build();
        };
    }
}
