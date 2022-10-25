package pl.edu.pg.eti.kask.rpg.social.network.repository;

import pl.edu.pg.eti.kask.rpg.DataStore;
import pl.edu.pg.eti.kask.rpg.repository.Repository;
import pl.edu.pg.eti.kask.rpg.social.network.entity.Comment;
import pl.edu.pg.eti.kask.rpg.social.network.entity.CommentType;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Dependent
public class CommentRepository {

    /**
     * Underlying data store. In future should be replaced with database connection.
     */
    private DataStore store;

    @Inject
    public CommentRepository(DataStore store) {
        this.store = store;
    }

    public Optional<Comment> find(Integer id) {
        return store.findCommentByCommentId(id);
    }

    public List<Comment> findAll() {
        return null;
    }

    public void create(Integer userId, Comment comment) {
        store.createComment(userId, comment);
    }

    public void delete(Integer commentId) {
        store.deleteComment(commentId);
    }

    public void update(Comment entity) {
        store.updateComment(entity);
    }

    public List<Comment> findUserComments(Integer userId){
        return store.findCommentsByUserId(userId);
    }
}
