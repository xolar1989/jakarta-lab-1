package pl.edu.pg.eti.kask.rpg.character.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.edu.pg.eti.kask.rpg.character.entity.Character;

import java.util.function.Function;

/**
 * GET character response. It contains all field that can be presented (but not necessarily changed) to the used. How
 * character is described is defined in {@link pl.edu.pg.eti.kask.rpg.character.entity.Character} and
 * {@link pl.edu.pg.eti.kask.rpg.creature.entity.Creature} classes.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetCharacterResponse {

    /**
     * Unique id identifying character.
     */
    private Long id;

    /**
     * Name of the character.
     */
    private String name;

    /**
     * Character's background story.
     */
    private String background;

    /**
     * Character's age.
     */
    private int age;

    /**
     * Character's strength.
     */
    private int strength;

    /**
     * Character's constitution.
     */
    private int constitution;

    /**
     * Character's charisma.
     */
    private int charisma;

    /**
     * Character's health.
     */
    private int health;

    /**
     * Character's level.
     */
    private int level;

    /**
     * Character's total experience.
     */
    private int experience;

    /**
     * Name of the characters's profession.
     */
    private String profession;

    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<Character, GetCharacterResponse> entityToDtoMapper() {
        return character -> GetCharacterResponse.builder()
                .id(character.getId())
                .name(character.getName())
                .age(character.getAge())
                .background(character.getBackground())
                .charisma(character.getCharisma())
                .constitution(character.getConstitution())
                .strength(character.getStrength())
                .experience(character.getExperience())
                .health(character.getHealth())
                .level(character.getLevel())
                .profession(character.getProfession().getName())
                .build();
    }

}
