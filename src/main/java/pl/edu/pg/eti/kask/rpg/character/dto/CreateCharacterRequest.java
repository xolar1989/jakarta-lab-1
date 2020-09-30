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
import pl.edu.pg.eti.kask.rpg.character.entity.Profession;
import pl.edu.pg.eti.kask.rpg.user.entity.User;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * POST character request. Contains only fields that can be set up byt the user while creating a new character.How
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
public class CreateCharacterRequest {

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
     * Name of the characters's profession.
     */
    private String profession;

    /**
     * @param professionFunction function for converting profession name to profession entity object
     * @param userSupplier       supplier for providing new character owner
     * @return mapper for convenient converting dto object to entity object
     */
    public static Function<CreateCharacterRequest, Character> dtoToEntityMapper(
            Function<String, Profession> professionFunction,
            Supplier<User> userSupplier) {
        return request -> Character.builder()
                .name(request.getName())
                .age(request.getAge())
                .background(request.getBackground())
                .charisma(request.getCharisma())
                .constitution(request.getConstitution())
                .profession(professionFunction.apply(request.getProfession()))
                .user(userSupplier.get())
                .build();
    }

}
