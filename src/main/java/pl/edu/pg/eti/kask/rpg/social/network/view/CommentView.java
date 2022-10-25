package pl.edu.pg.eti.kask.rpg.social.network.view;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.rpg.social.network.entity.Comment;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;
import pl.edu.pg.eti.kask.rpg.social.network.model.CommentModel;
import pl.edu.pg.eti.kask.rpg.social.network.model.UserModel;
import pl.edu.pg.eti.kask.rpg.social.network.service.UserCommentService;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@ViewScoped
@Named
public class CommentView implements Serializable {

    private UserCommentService userCommentService;

    /**
     * User id.
     */
    @Setter
    @Getter
    private Integer commentId;

    @Getter
    private CommentModel commentModel;

    public void init() throws IOException {

        Optional<Comment> comment = userCommentService.findById(commentId);
        if (comment.isPresent()) {
            this.commentModel = CommentModel.entityToModelMapper().apply(comment.get());
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Character not found");
        }


    }

}
