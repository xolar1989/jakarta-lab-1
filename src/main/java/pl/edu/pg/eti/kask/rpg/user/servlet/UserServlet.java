package pl.edu.pg.eti.kask.rpg.user.servlet;

import pl.edu.pg.eti.kask.rpg.servlet.MimeTypes;
import pl.edu.pg.eti.kask.rpg.user.context.UserContext;
import pl.edu.pg.eti.kask.rpg.user.dto.GetUserResponse;
import pl.edu.pg.eti.kask.rpg.user.entity.User;
import pl.edu.pg.eti.kask.rpg.user.service.UserService;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Servlet for returning user's name from the active session if present.
 */
@WebServlet(urlPatterns = UserServlet.Paths.USER)
public class UserServlet extends HttpServlet {

    /**
     * Service for user entity operations.
     */
    private UserService service;

    /**
     * Authorized user context.
     */
    private UserContext context;

    @Inject
    public UserServlet(UserService service, UserContext context) {
        this.service = service;
        this.context = context;
    }

    /**
     * Definition of paths supported by this servlet. Separate inner class provides composition for static fields.
     */
    public static class Paths {

        /**
         * Specified portrait for download and upload.
         */
        public static final String USER = "/api/user";

    }

    /**
     * JSON-B mapping object. According to open liberty documentation creating those is expensive. The JSON-B is only
     * one of many solutions. JSON strings can be build by hand {@link StringBuilder} or with JSON-P API. Both JSON-B
     * and JSON-P are part of Jakarta EE whereas JSON-B is newer standard.
     */
    private final Jsonb jsonb = JsonbBuilder.create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String principal = context.getPrincipal();
        response.setContentType(MimeTypes.APPLICATION_JSON);
        if (principal != null) {
            Optional<User> user = service.find(principal);
            if (user.isPresent()) {
                response.getWriter().write(jsonb.toJson(GetUserResponse.entityToDtoMapper().apply(user.get())));
                return;
            }
        }
        response.getWriter().write("{}");//Empty JSON object.
    }

}
