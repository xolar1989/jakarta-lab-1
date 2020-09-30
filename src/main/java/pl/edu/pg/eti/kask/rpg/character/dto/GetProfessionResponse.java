package pl.edu.pg.eti.kask.rpg.character.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

import java.util.Map;

/**
 * GET profession response. Described details about selected profession. Can be used to present description while
 * character creation or on character's stat page. How profession is described is defined in
 * {@link pl.edu.pg.eti.kask.rpg.character.entity.Profession}.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetProfessionResponse {

    /**
     * Describes single skill. Returning profession description without skills list would no give all required
     * information. Forcing to return list of skills in separate request would be unnecessary transfer growth. How
     * skills are described is defined in {@link pl.edu.pg.eti.kask.rpg.character.entity.Skill}.
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class Skill {

        /**
         * Name of the skill.
         */
        private String name;

        /**
         * Description of the skill.
         */
        private String description;

    }

    /**
     * Name of the profession.
     */
    private String name;

    /**
     * Set of skills available to this profession on different levels.
     */
    @Singular
    private Map<Integer, Skill> skills;

}
