package pl.edu.pg.eti.kask.rpg.datastore;

import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.rpg.character.entity.Character;
import pl.edu.pg.eti.kask.rpg.character.entity.Profession;
import pl.edu.pg.eti.kask.rpg.serialization.CloningUtility;
import pl.edu.pg.eti.kask.rpg.user.entity.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * For the sake of simplification instead of using real database this example is using an data source object which
 * should be put in servlet context in a single instance. In order to avoid
 * {@link java.util.ConcurrentModificationException} all methods are synchronized. Normally synchronization would be
 * carried on by the database server.
 */
@Log
@ApplicationScoped
public class DataStore {

    /**
     * Set of all available professions.
     */
    private Set<Profession> professions = new HashSet<>();

    /**
     * Set of all characters.
     */
    private Set<Character> characters = new HashSet<>();

    /**
     * Set of all users.
     */
    private Set<User> users = new HashSet<>();

    /**
     * Seeks for all professions.
     *
     * @return list (can be empty) of all professions
     */
    public synchronized List<Profession> findAllProfessions() {
        return new ArrayList<>(professions);
    }

    /**
     * Seeks for the profession in the memory storage.
     *
     * @param name name of the profession
     * @return container (can be empty) with profession if present
     */
    public Optional<Profession> findProfession(String name) {
        return professions.stream()
                .filter(profession -> profession.getName().equals(name))
                .findFirst()
                .map(CloningUtility::clone);
    }

    /**
     * Stores new profession.
     *
     * @param profession new profession to be stored
     * @throws IllegalArgumentException if profession with provided name already exists
     */
    public synchronized void createProfession(Profession profession) throws IllegalArgumentException {
        findProfession(profession.getName()).ifPresentOrElse(
                original -> {
                    throw new IllegalArgumentException(
                            String.format("The profession name \"%s\" is not unique", profession.getName()));
                },
                () -> professions.add(profession));
    }

    /**
     * Seeks for all characters.
     *
     * @return list (can be empty) of all characters
     */
    public synchronized List<Character> findAllCharacters() {
        return characters.stream().map(CloningUtility::clone).collect(Collectors.toList());
    }

    /**
     * Seeks for single character.
     *
     * @param id character's id
     * @return container (can be empty) with character
     */
    public synchronized Optional<Character> findCharacter(Long id) {
        return characters.stream()
                .filter(character -> character.getId().equals(id))
                .findFirst()
                .map(CloningUtility::clone);
    }

    /**
     * Stores new character.
     *
     * @param character new character
     */
    public synchronized void createCharacter(Character character) throws IllegalArgumentException {
        character.setId(findAllCharacters().stream().mapToLong(Character::getId).max().orElse(0) + 1);
        characters.add(character);
    }

    /**
     * Updates existing character.
     *
     * @param character character to be updated
     * @throws IllegalArgumentException if character with the same id does not exist
     */
    public synchronized void updateCharacter(Character character) throws IllegalArgumentException {
        findCharacter(character.getId()).ifPresentOrElse(
                original -> {
                    characters.remove(original);
                    characters.add(character);
                },
                () -> {
                    throw new IllegalArgumentException(
                            String.format("The character with id \"%d\" does not exist", character.getId()));
                });
    }

    /**
     * Deletes existing character.
     *
     * @param id character's id
     * @throws IllegalArgumentException if character with provided id does not exist
     */
    public synchronized void deleteCharacter(Long id) throws IllegalArgumentException {
        findCharacter(id).ifPresentOrElse(
                original -> characters.remove(original),
                () -> {
                    throw new IllegalArgumentException(
                            String.format("The character with id \"%d\" does not exist", id));
                });
    }

    /**
     * Seeks for single user.
     *
     * @param login user's login
     * @return container (can be empty) with user
     */
    public synchronized Optional<User> findUser(String login) {
        return users.stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst()
                .map(CloningUtility::clone);
    }

    /**
     * Seeks for single user using login and password. Can be use in authentication module.
     *
     * @param login    user's login
     * @param password user's password
     * @return container (can be empty) with user
     */
    public synchronized Optional<User> findUser(String login, String password) throws IllegalArgumentException {
        return users.stream()
                .filter(user -> user.getLogin().equals(login))
                .filter(users -> users.getPassword().equals(password))
                .findFirst()
                .map(CloningUtility::clone);
    }

    /**
     * Stores new user.
     *
     * @param user new user to be stored
     * @throws IllegalArgumentException if user with provided login already exists
     */
    public synchronized void createUser(User user) throws IllegalArgumentException {
        findUser(user.getLogin()).ifPresentOrElse(
                original -> {
                    throw new IllegalArgumentException(
                            String.format("The user login \"%s\" is not unique", user.getLogin()));
                },
                () -> users.add(user));
    }

    /**
     * Get stream to be used (for filtering, sorting, etc) in repositories.
     *
     * @return character's stream
     */
    public Stream<Character> getCharacterStream() {
        return characters.stream();
    }
}
