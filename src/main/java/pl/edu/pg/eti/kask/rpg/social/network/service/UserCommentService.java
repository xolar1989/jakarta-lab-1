package pl.edu.pg.eti.kask.rpg.social.network.service;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import pl.edu.pg.eti.kask.rpg.social.network.entity.Comment;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;
import pl.edu.pg.eti.kask.rpg.social.network.repository.CommentRepository;
import pl.edu.pg.eti.kask.rpg.social.network.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


@ApplicationScoped
@NoArgsConstructor//Empty constructor is required for creating proxy while CDI injection.
public class UserCommentService {

    /**
     * Repository for user entity.
     */
    private UserRepository repository;

    private CommentRepository commentRepository;

    @Inject
    public UserCommentService(UserRepository repository, CommentRepository commentRepository) {
        this.repository = repository;
        this.commentRepository = commentRepository;
    }

    public void createCommentForUser(Comment comment, User user) {
        commentRepository.create(user.getId(), comment);
    }

    public List<Comment> getUserComments(User user) {
        return commentRepository.findUserComments(user.getId());
    }

    public Optional<Comment> getUserComment(User user, Integer commentId) {
        return getUserComments(user).stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findFirst();
    }

    public void deleteComment(Integer commentId) {
        System.out.println("dmksmndkkd");
        System.out.println(commentId);
        commentRepository.delete(commentId);
    }

    public void updateComment(Comment comment) {
        commentRepository.update(comment);
    }

    public Optional<Comment> findById(Integer id) {
        return commentRepository.find(id);
    }

    public List<Comment> findAllComments() {
        return commentRepository.findAllComments();
    }
}
