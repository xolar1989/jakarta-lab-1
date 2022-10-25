package pl.edu.pg.eti.kask.rpg.social.network.view;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;
import pl.edu.pg.eti.kask.rpg.social.network.model.UserModel;
import pl.edu.pg.eti.kask.rpg.social.network.service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@ViewScoped
@Named
public class UserView implements Serializable {

    private UserService userService;

    /**
     * User id.
     */
    @Setter
    @Getter
    private Integer id;

    private UserModel userModel;

    @Inject
    public UserView(UserService userService) {
        this.userService = userService;
    }

    public void init() throws IOException {

        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            this.userModel = UserModel.entityToModelMapper().apply(user.get());
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Character not found");
        }


    }

    public UserModel getUserModel() {
        return this.userModel;
    }
}
