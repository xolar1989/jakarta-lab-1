package pl.edu.pg.eti.kask.rpg.user.repository;

import pl.edu.pg.eti.kask.rpg.datastore.DataStore;
import pl.edu.pg.eti.kask.rpg.repository.Repository;
import pl.edu.pg.eti.kask.rpg.user.entity.User;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Repository for User entity. Repositories should be used in business layer (e.g.: in services).
 */
@Dependent
public class UserRepository implements Repository<User, String> {

    /**
     * Underlying data store. In future should be replaced with database connection.
     */
    private DataStore store;

    /**
     * @param store data store
     */
    @Inject
    public UserRepository(DataStore store) {
        this.store = store;
    }

    @Override
    public Optional<User> find(String id) {
        return store.findUser(id);
    }

    @Override
    public List<User> findAll() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void create(User entity) {
        store.createUser(entity);
    }

    @Override
    public void delete(User entity) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void update(User entity) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    /**
     * Seeks for single user using login and password. Can be use in authentication module.
     *
     * @param login    user's login
     * @param password user's password (hash)
     * @return container (can be empty) with user
     */
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return store.findUser(login, password);
    }

}
