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

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * GET characters response. Contains list of available characters. Can be used to list particular user's characters as
 * well as all characters in the game.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetCharactersResponse {

    /**
     * Represents single character in list.
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class Character {

        /**
         * Unique id identifying character.
         */
        private Long id;

        /**
         * Name of the character.
         */
        private String name;

    }

    /**
     * Name of the selected characters.
     */
    @Singular
    private List<Character> characters;

    /**
     * @return mapper for convenient converting entity object to dto object
     */
    public static Function<Collection<pl.edu.pg.eti.kask.rpg.character.entity.Character>, GetCharactersResponse> entityToDtoMapper() {
        return characters -> {
            GetCharactersResponse.GetCharactersResponseBuilder response = GetCharactersResponse.builder();
            characters.stream()
                    .map(character -> Character.builder()
                            .id(character.getId())
                            .name(character.getName())
                            .build())
                    .forEach(response::character);
            return response.build();
        };
    }

}
