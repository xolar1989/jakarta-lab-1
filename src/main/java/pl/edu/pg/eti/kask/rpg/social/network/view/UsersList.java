package pl.edu.pg.eti.kask.rpg.social.network.view;

import pl.edu.pg.eti.kask.rpg.social.network.model.UsersModel;
import pl.edu.pg.eti.kask.rpg.social.network.service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.Serializable;


/**
 * View bean for rendering list of users.
 */
@RequestScoped
@Named
public class UsersList implements Serializable {

    private UserService userService;

    private UsersModel usersModel;

    @Inject
    public UsersList(UserService userService) {
        this.userService = userService;
    }

    public UsersModel getUsersModel(){
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();

        System.out.println(viewId);
        if(usersModel == null){
            usersModel = UsersModel.entityToModelMapper().apply(userService.findAllUsers());
        }

        return usersModel;
    }

    public String deleteAction() {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        System.out.println("kanapka");
        return viewId + "?faces-redirect=true&includeViewParams=true";
    }
}
