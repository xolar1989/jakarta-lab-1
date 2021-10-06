package pl.edu.pg.eti.kask.rpg.character.repository;

import pl.edu.pg.eti.kask.rpg.character.entity.Character;
import pl.edu.pg.eti.kask.rpg.datastore.DataStore;
import pl.edu.pg.eti.kask.rpg.repository.Repository;
import pl.edu.pg.eti.kask.rpg.serialization.CloningUtility;
import pl.edu.pg.eti.kask.rpg.user.entity.User;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository for character entity. Repositories should be used in business layer (e.g.: in services).
 */
@Dependent
public class CharacterRepository implements Repository<Character, Long> {

    /**
     * Underlying data store. In future should be replaced with database connection.
     */
    private DataStore store;

    /**
     * @param store data store
     */
    @Inject
    public CharacterRepository(DataStore store) {
        this.store = store;
    }

    @Override
    public Optional<Character> find(Long id) {
        return store.findCharacter(id);
    }

    @Override
    public List<Character> findAll() {
        return store.findAllCharacters();
    }

    @Override
    public void create(Character entity) {
        store.createCharacter(entity);
    }

    @Override
    public void delete(Character entity) {
        store.deleteCharacter(entity.getId());
    }

    @Override
    public void update(Character entity) {
        store.updateCharacter(entity);
    }

    /**
     * Seeks for single user's character.
     *
     * @param id   character's id
     * @param user characters's owner
     * @return container (can be empty) with character
     */
    public Optional<Character> findByIdAndUser(Long id, User user) {
        return store.findAllCharacters().stream()
                .filter(character -> character.getUser().equals(user))
                .filter(character -> character.getId().equals(id))
                .findFirst()
                .map(CloningUtility::clone);
    }

    /**
     * Seeks for all user's characters.
     *
     * @param user characters' owner
     * @return list (can be empty) of user's characters
     */
    public List<Character> findAllByUser(User user) {
        return store.findAllCharacters().stream()
                .filter(character -> character.getUser().equals(user))
                .map(CloningUtility::clone)
                .collect(Collectors.toList());
    }

}
