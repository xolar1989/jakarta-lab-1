package pl.edu.pg.eti.kask.rpg.character.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.rpg.character.entity.Profession;
import pl.edu.pg.eti.kask.rpg.character.repository.ProfessionRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Service layer for all business actions regarding character's profession entity.
 */
@ApplicationScoped
@NoArgsConstructor//Empty constructor is required for creating proxy while CDI injection.
public class ProfessionService {

    /**
     * Repository for profession entity.
     */
    private ProfessionRepository repository;

    /**
     * @param repository repository for profession entity
     */
    @Inject
    public ProfessionService(ProfessionRepository repository) {
        this.repository = repository;
    }

    /**
     * @param name name of the profession
     * @return container with profession entity
     */
    public Optional<Profession> find(String name) {
        return repository.find(name);
    }

    /**
     * Stores new profession in the data store.
     *
     * @param profession new profession to be saved
     */
    public void create(Profession profession) {
        repository.create(profession);
    }

}
