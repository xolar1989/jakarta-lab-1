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

import java.util.function.BiFunction;

/**
 * PUT character request. Contains all fields that can be updated by the user. .How character is described is defined in
 * {@link pl.edu.pg.eti.kask.rpg.character.entity.Character} and {@link pl.edu.pg.eti.kask.rpg.creature.entity.Creature}
 * classes.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdateCharacterRequest {

    /**
     * Character's name.
     */
    private String name;

    /**
     * Character's background story.
     */
    private String background;

    /**
     * Character's name.
     */
    private int age;

    /**
     * @return updater for convenient updating entity object using dto object
     */
    public static BiFunction<Character, UpdateCharacterRequest, Character> dtoToEntityUpdater() {
        return (character, request) -> {
            character.setName(request.getName());
            character.setAge(request.getAge());
            character.setBackground(request.getBackground());
            return character;
        };
    }
}
