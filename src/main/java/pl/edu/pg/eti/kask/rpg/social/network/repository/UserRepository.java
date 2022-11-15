package pl.edu.pg.eti.kask.rpg.social.network.repository;

import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.rpg.repository.Repository;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * Repository for User entity. Repositories should be used in business layer (e.g.: in services).
 */
@RequestScoped
@Log
public class UserRepository implements Repository<User, Integer> {

    /**
     * Underlying data store. In future should be replaced with database connection.
     */
    /**
     * Connection with the database (not thread safe).
     */
    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<User> find(Integer id) {
        log.info(String.format("EntityManager for %s %s find", this.getClass(), em));
        return Optional.ofNullable(em.find(User.class, id));
    }

    public Optional<User> findById(Integer id){
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public void create(User entity) {
        log.info(String.format("EntityManager for %s %s create", this.getClass(), em));
        em.persist(entity);
    }

    @Override
    public void delete(User entity) {
        em.remove(em.find(User.class, entity.getId()));
    }

    @Override
    public void update(User entity) {
        em.merge(entity);
    }

    @Override
    public void detach(User entity) {
        em.detach(entity);
    }


}
