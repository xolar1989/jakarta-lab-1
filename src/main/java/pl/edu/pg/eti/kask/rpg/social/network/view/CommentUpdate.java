package pl.edu.pg.eti.kask.rpg.social.network.view;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.rpg.social.network.entity.Comment;
import pl.edu.pg.eti.kask.rpg.social.network.entity.CommentType;
import pl.edu.pg.eti.kask.rpg.social.network.model.CommentModel;
import pl.edu.pg.eti.kask.rpg.social.network.model.CommentTypeModel;
import pl.edu.pg.eti.kask.rpg.social.network.model.UpdateCommentModel;
import pl.edu.pg.eti.kask.rpg.social.network.service.UserCommentService;
import pl.edu.pg.eti.kask.rpg.social.network.service.UserService;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@ViewScoped
@Named
public class CommentUpdate implements Serializable {

    private UserCommentService userCommentService;

    private UserService userService;

    @Setter
    @Getter
    private Integer commentId;

    @Getter
    private UpdateCommentModel updateCommentModel;

    @Getter
    private List<CommentTypeModel> commentTypes = List.of(
            CommentTypeModel.entityToModelMapper().apply(CommentType.HATE_COMMENT),
            CommentTypeModel.entityToModelMapper().apply(CommentType.NORMAL_COMMENT)
    );


    @Inject
    public CommentUpdate(UserCommentService userCommentService, UserService userService) {
        this.userCommentService = userCommentService;
        this.userService = userService;
    }


    public void init() throws IOException {

        System.out.println(commentId);
        Optional<Comment> comment = userCommentService.findById(commentId);
        if (comment.isPresent()) {
            this.updateCommentModel = UpdateCommentModel.builder().content(comment.get().getContent()).build();
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Character not found");
        }
    }

    public String updateComment() {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();

        Comment fromDatabase = userCommentService.findById(commentId).orElseThrow();

        System.out.println(updateCommentModel);
        Comment comment = UpdateCommentModel
                .modelToEntityMapper(() -> userService.findById(fromDatabase.getUser().getId()).get())
                .apply(updateCommentModel);
        comment.setId(commentId);
        userCommentService.updateComment(comment);
//        userCommentService.updateComment();
        return "/users/user_comments_list.xhtml" + "?faces-redirect=true&includeViewParams=true";
    }
}
