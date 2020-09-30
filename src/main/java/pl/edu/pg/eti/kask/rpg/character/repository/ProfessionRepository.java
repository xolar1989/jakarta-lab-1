package pl.edu.pg.eti.kask.rpg.character.repository;

import pl.edu.pg.eti.kask.rpg.character.entity.Profession;
import pl.edu.pg.eti.kask.rpg.datastore.DataStore;
import pl.edu.pg.eti.kask.rpg.repository.Repository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Repository for profession entity. Repositories should be used in business layer (e.g.: in services).
 */
@Dependent
public class ProfessionRepository implements Repository<Profession, String> {

    /**
     * Underlying data store. In future should be replaced with database connection.
     */
    private DataStore store;

    /**
     * @param store data store
     */
    @Inject
    public ProfessionRepository(DataStore store) {
        this.store = store;
    }


    @Override
    public Optional<Profession> find(String id) {
        return store.findProfession(id);
    }

    @Override
    public List<Profession> findAll() {
        return store.findAllProfessions();
    }

    @Override
    public void create(Profession entity) {
        store.createProfession(entity);
    }

    @Override
    public void delete(Profession entity) {
        throw new UnsupportedOperationException("Operation not implemented.");
    }

    @Override
    public void update(Profession entity) {
        throw new UnsupportedOperationException("Operation not implemented.");
    }

}
