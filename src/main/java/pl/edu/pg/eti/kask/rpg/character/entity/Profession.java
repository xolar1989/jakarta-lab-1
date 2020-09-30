package pl.edu.pg.eti.kask.rpg.character.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * Entity class for game characters' professions (classes). Describes name of the profession and skills available on
 * different levels.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class Profession implements Serializable {

    /**
     * Name of the profession.
     */
    private String name;

    /**
     * Set of skills available on different levels. While leveling up, character gains access to new skills. One skill
     * every limit level. There is no rule which levels are limit ones.
     */
    private Map<Integer, Skill> skills;

}
