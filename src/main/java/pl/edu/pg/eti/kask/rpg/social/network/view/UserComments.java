package pl.edu.pg.eti.kask.rpg.social.network.view;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import pl.edu.pg.eti.kask.rpg.social.network.entity.Comment;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;
import pl.edu.pg.eti.kask.rpg.social.network.model.CommentModel;
import pl.edu.pg.eti.kask.rpg.social.network.model.UserCommentsModel;
import pl.edu.pg.eti.kask.rpg.social.network.model.UserModel;
import pl.edu.pg.eti.kask.rpg.social.network.service.UserCommentService;
import pl.edu.pg.eti.kask.rpg.social.network.service.UserService;

import javax.enterprise.context.RequestScoped;
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
public class UserComments implements Serializable {

    private UserService userService;

    private UserCommentService userCommentService;

    @Getter
    private UserCommentsModel userCommentsModel;

    /**
     * User id.
     */
    @Setter
    @Getter
    private Integer userId;

    @Inject
    public UserComments(UserService userService, UserCommentService userCommentService) {
        this.userService = userService;
        this.userCommentService = userCommentService;
    }

    public void init() throws IOException {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent()) {
            List<Comment> comments = userCommentService.getUserComments(user.get());
            this.userCommentsModel = UserCommentsModel.entityToModelMapper().apply(comments);
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Character not found");
        }


    }

    public String deleteAction(CommentModel commentModel) {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        System.out.println(viewId);
        System.out.println("delete leci z delete musi to wyjebać");
        System.out.println(commentModel.getId());
        userCommentService.deleteComment(commentModel.getId());
        return viewId +"?faces-redirect=true&includeViewParams=true";
    }


}
