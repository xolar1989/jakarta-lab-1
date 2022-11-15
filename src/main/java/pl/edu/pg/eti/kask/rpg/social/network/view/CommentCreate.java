package pl.edu.pg.eti.kask.rpg.social.network.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.rpg.social.network.entity.Comment;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;
import pl.edu.pg.eti.kask.rpg.social.network.model.CommentModel;
import pl.edu.pg.eti.kask.rpg.social.network.model.CreateCommentModel;
import pl.edu.pg.eti.kask.rpg.social.network.service.UserCommentService;
import pl.edu.pg.eti.kask.rpg.social.network.service.UserService;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ConversationScoped
@Named
@NoArgsConstructor
@Log
public class CommentCreate implements Serializable {

    private UserCommentService userCommentService;

    private UserService userService;

    /**
     * user id.
     */
    @Setter
    @Getter
    private Integer userId;

    /**
     * Injected conversation.
     */
    private Conversation conversation;

    @Getter
    private CreateCommentModel createCommentModel;

    @Inject
    public CommentCreate(UserCommentService userCommentService, Conversation conversation, UserService userService) {
        this.userCommentService = userCommentService;
        this.conversation = conversation;
        this.userService = userService;
    }

    public void init() {
        if (conversation.isTransient()) {
            createCommentModel = CreateCommentModel.builder().build();
            conversation.begin();
        }
    }

    public String createComment(CreateCommentModel model) {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        System.out.println(userId);
        if (model != null) {

            userService.findById(userId)
                    .ifPresent(user1 -> {
                        Comment comment = Comment.builder()
                                .content(model.getContent())
                                .user(user1)
                                .build();
                        userCommentService.createCommentForUser(comment);
                    });

        }
        return viewId + "?faces-redirect=true&includeViewParams=true";
    }
}
