package pl.edu.pg.eti.kask.rpg.social.network.entity;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CommentType implements Serializable {

    public static CommentType HATE_COMMENT = new CommentType("hate");

    public static CommentType NORMAL_COMMENT = new CommentType("normal");

    private String type;

    public static CommentType fromString(String value){
        if(HATE_COMMENT.type.equals(value)){
            return HATE_COMMENT;
        }
        else if(NORMAL_COMMENT.type.equals(value)){
            return NORMAL_COMMENT;
        }
        throw new IllegalArgumentException("TYPE NOT CORRECT");
    }


}
