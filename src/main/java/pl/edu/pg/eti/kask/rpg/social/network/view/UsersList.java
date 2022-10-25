package pl.edu.pg.eti.kask.rpg.social.network.view;

import pl.edu.pg.eti.kask.rpg.social.network.service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;


/**
 * View bean for rendering list of users.
 */
@RequestScoped
@Named
public class UsersList implements Serializable {

    private UserService userService;



}
