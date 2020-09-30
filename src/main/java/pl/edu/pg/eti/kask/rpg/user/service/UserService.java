package pl.edu.pg.eti.kask.rpg.user.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.rpg.user.entity.User;
import pl.edu.pg.eti.kask.rpg.user.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Service layer for all business actions regarding user entity.
 */
@ApplicationScoped
@NoArgsConstructor//Empty constructor is required for creating proxy while CDI injection.
public class UserService {

    /**
     * Mock of the database. Should be replaced with repository layer.
     */
    private UserRepository repository;

    @Inject
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * @param login user's login
     * @return container with user
     */
    public Optional<User> find(String login) {
        return repository.find(login);
    }

    /**
     * Seeks for single user using login and password. Can be use in authentication module.
     *
     * @param login    user's login
     * @param password user's password (hash)
     * @return container (can be empty) with user
     */
    public Optional<User> find(String login, String password) {
        return repository.findByLoginAndPassword(login, password);
    }

    /**
     * Stores new user in the storage.
     *
     * @param user new user
     */
    public void create(User user) {
        repository.create(user);
    }

}
