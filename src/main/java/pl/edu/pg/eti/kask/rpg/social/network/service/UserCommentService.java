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
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;


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

    @Transactional
    public void createCommentForUser(Comment comment) {
        commentRepository.create(comment);
        repository.find(comment.getUser().getId()).ifPresent(user -> user.getComments().add(comment));
    }

    public List<Comment> getUserComments(User user) {
        return commentRepository.findAll()
                .stream()
                .filter(comment -> comment.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    public Optional<Comment> getUserComment(User user, Integer commentId) {
        return getUserComments(user).stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findFirst();
    }

    @Transactional
    public void deleteComment(Integer commentId) {
        System.out.println("dmksmndkkd");
        System.out.println(commentId);
        Comment comment = commentRepository.find(commentId).orElseThrow();
        comment.getUser().getComments().remove(comment);
        commentRepository.delete(comment);
    }

    @Transactional
    public void updateComment(Comment comment) {
        Comment original = commentRepository.find(comment.getId()).orElseThrow();
        commentRepository.detach(original);
        if(!original.getUser().getId().equals(comment.getUser().getId())){
            original.getUser().getComments().removeIf(commentToRemove -> commentToRemove.getId().equals(comment.getId()));
            repository.find(comment.getUser().getId()).ifPresent(user -> user.getComments().add(comment));
        }
        commentRepository.update(comment);
    }

    public Optional<Comment> findById(Integer id) {
        return commentRepository.find(id);
    }

    public List<Comment> findAllComments() {
        return commentRepository.findAll();
    }
}
