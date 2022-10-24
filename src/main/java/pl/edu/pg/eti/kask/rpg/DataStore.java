package pl.edu.pg.eti.kask.rpg;

import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.rpg.serialization.CloningUtility;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
     * Set of all users.
     */
    private Set<User> users = new HashSet<>();


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
     * Seeks for all users.
     *
     * @return collection of all users
     */
    public synchronized List<User> findAllUsers() {
        return users.stream()
                .map(CloningUtility::clone)
                .collect(Collectors.toList());
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
                () -> {
                    user.setId(findAllUsers().stream()
                            .mapToInt(User::getId)
                            .max().orElse(0) + 1);
                    users.add(CloningUtility.clone(user));
                });
    }

    public synchronized void updateUser(User user) throws IllegalArgumentException {
        findUserById(user.getId()).ifPresentOrElse(
                original -> {
                    users.remove(original);
                    users.add(CloningUtility.clone(user));
                },
                () -> {
                    throw new IllegalArgumentException(
                            String.format("The user with id \"%d\" does not exist", user.getId()));
                });
    }

    public synchronized Optional<User> findUserById(Integer id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .map(CloningUtility::clone);
    }


    public synchronized void deleteUser(Integer id) throws IllegalArgumentException {
        findUserById(id).ifPresentOrElse(
                original -> users.remove(original),
                () -> {
                    throw new IllegalArgumentException(
                            String.format("The user with id \"%d\" does not exist", id));
                });
    }
}
