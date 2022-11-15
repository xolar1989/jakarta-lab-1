package pl.edu.pg.eti.kask.rpg.social.network.repository;

import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.rpg.repository.Repository;
import pl.edu.pg.eti.kask.rpg.social.network.entity.Comment;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@RequestScoped
@Log
public class CommentRepository implements Repository<Comment,Integer> {

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
    public Optional<Comment> find(Integer id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    public List<Comment> findAll() {
        return em.createQuery("select c from Comment c", Comment.class).getResultList();
    }

    @Override
    public void create(Comment entity) {
        em.persist(entity);
    }

    @Override
    public void delete(Comment entity) {
        Comment comment1 = em.find(Comment.class, entity.getId());
        em.remove(comment1);
    }

    @Override
    public void update(Comment entity) {
        em.merge(entity);
    }

    @Override
    public void detach(Comment entity) {
        em.detach(entity);
    }

}
