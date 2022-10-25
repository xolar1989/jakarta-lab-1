package pl.edu.pg.eti.kask.rpg.social.network.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Comment implements Serializable {

    private Integer id;

    private String content;

    private Integer createdById;
}
