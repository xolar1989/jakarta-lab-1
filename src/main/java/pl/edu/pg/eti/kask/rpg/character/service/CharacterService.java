package pl.edu.pg.eti.kask.rpg.character.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.rpg.character.entity.Character;
import pl.edu.pg.eti.kask.rpg.character.repository.CharacterRepository;
import pl.edu.pg.eti.kask.rpg.user.context.UserContext;
import pl.edu.pg.eti.kask.rpg.user.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for all business actions regarding character entity.
 */
@ApplicationScoped
@NoArgsConstructor//Empty constructor is required for creating proxy while CDI injection.
public class CharacterService {

    /**
     * Repository for character entity.
     */
    private CharacterRepository characterRepository;

    /**
     * Repository for user entity.
     */
    private UserRepository userRepository;

    /**
     * Authenticated user session context.
     */
    private UserContext userContext;

    /**
     * @param characterRepository repository for character entity
     * @param userRepository      repository for user entity
     * @param userContext         authenticated user session context
     */
    @Inject
    public CharacterService(CharacterRepository characterRepository, UserRepository userRepository, UserContext userContext) {
        this.characterRepository = characterRepository;
        this.userRepository = userRepository;
        this.userContext = userContext;
    }

    /**
     * Finds single character.
     *
     * @param id character's id
     * @return container with character
     */
    public Optional<Character> find(Long id) {
        return characterRepository.find(id);
    }

    /**
     * @return all available characters of the authenticated user
     */
    public Optional<Character> findForCallerPrincipal(Long id) {
        return characterRepository.findByIdAndUser(id, userRepository.find(userContext.getPrincipal()).orElseThrow());
    }

    /**
     * @return all available characters
     */
    public List<Character> findAll() {
        return characterRepository.findAll();
    }

    /**
     * @return all available characters of the authenticated user
     */
    public List<Character> findAllForCallerPrincipal() {
        return characterRepository.findAllByUser(userRepository.find(userContext.getPrincipal()).orElseThrow());
    }

    /**
     * Creates new character.
     *
     * @param character new character
     */
    public void create(Character character) {
        characterRepository.create(character);
    }

    /**
     * Assigns currently logged user to passed new character and saves it in data store,
     *
     * @param character new character to be saved
     */
    public void createForCallerPrincipal(Character character) {
        character.setUser(userRepository.find(userContext.getPrincipal()).orElseThrow());
        characterRepository.create(character);
    }

    /**
     * Updates existing character.
     *
     * @param character character to be updated
     */
    public void update(Character character) {
        characterRepository.update(character);
    }

    /**
     * Deletes existing character.
     *
     * @param character existing character's id to be deleted
     */
    public void delete(Long character) {
        characterRepository.delete(characterRepository.find(character).orElseThrow());
    }

    /**
     * Updates portrait of the character.
     *
     * @param id character's id
     * @param is input stream containing new portrait
     */
    public void updatePortrait(Long id, InputStream is) {
        characterRepository.find(id).ifPresent(character -> {
            try {
                character.setPortrait(is.readAllBytes());
                characterRepository.update(character);
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        });
    }

}
