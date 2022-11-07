package pl.edu.pg.eti.kask.rpg.social.network.servlet;

import pl.edu.pg.eti.kask.rpg.servlet.HttpHeaders;
import pl.edu.pg.eti.kask.rpg.servlet.MimeTypes;
import pl.edu.pg.eti.kask.rpg.servlet.ServletUtility;
import pl.edu.pg.eti.kask.rpg.servlet.UrlFactory;
import pl.edu.pg.eti.kask.rpg.social.network.dto.CreateUserRequest;
import pl.edu.pg.eti.kask.rpg.social.network.dto.GetUserResponse;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;
import pl.edu.pg.eti.kask.rpg.social.network.service.UserService;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Optional;

/**
 * Servlet for returning user's name from the active session if present.
 */
@WebServlet(urlPatterns = {
        UserServlet.Paths.USERS + "/*"
})
public class UserServlet extends HttpServlet {

    /**
     * Service for user entity operations.
     */
    private UserService service;

    @Inject
    public UserServlet(UserService service) {
        this.service = service;
    }

    /**
     * Definition of paths supported by this servlet. Separate inner class provides composition for static fields.
     */
    public static class Paths {

        /**
         * Specified portrait for download and upload.
         */
        public static final String USERS = "/api2/users";

        public static final String USER_IMAGE = "/api2/user/image";
    }

    public static class Patterns {
        public static final String USERS = "^/?$";
        //        public static final String USER = "^/[a-zA-Z0-9]+/?$";
        public static final String USER = "^/[0-9]+/?$";

        public static final String IMAGE_NUMBER = "^/[0-9]+/?$";
    }

    static class Parameters {
        static final String IMAGE = "image";
    }

    /**
     * JSON-B mapping object. According to open liberty documentation creating those is expensive. The JSON-B is only
     * one of many solutions. JSON strings can be build by hand {@link StringBuilder} or with JSON-P API. Both JSON-B
     * and JSON-P are part of Jakarta EE whereas JSON-B is newer standard.
     */
    private final Jsonb jsonb = JsonbBuilder.create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.USERS.equals(servletPath)) {
            if (path.matches(Patterns.USER)) {
                getUser(request, response);
                return;
            } else if (path.matches(Patterns.USERS)) {
                getUsers(request, response);
                return;
            }
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        response.getWriter().write("{}");//Empty JSON object.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.USERS.equals(servletPath)) {
            if (path.matches(Patterns.USERS)) {
                postUser(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        if (Paths.USER_IMAGE.equals(request.getServletPath())) {
            if (path.matches(Patterns.IMAGE_NUMBER)) {
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }


    private void postUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CreateUserRequest requestBody = jsonb.fromJson(request.getInputStream(), CreateUserRequest.class);

        User user = CreateUserRequest.dtoToEntityMapper().apply(requestBody);

        try {
            service.create(user);
            //When creating new resource, its location should be returned.
            response.addHeader(HttpHeaders.LOCATION,
                    UrlFactory.createUrl(request, Paths.USERS, user.getId().toString()));
            //When creating new resource, appropriate code should be set.
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void getUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(MimeTypes.APPLICATION_JSON);
        response.getWriter()
                .write(jsonb.toJson(service.findAll()));

    }

    private void getUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String stringId = ServletUtility.parseRequestPath(request).replaceAll("/", "");
        Integer id = Integer.valueOf(stringId);
        Optional<User> user = service.findById(id);

        if (user.isPresent()) {
            response.setContentType(MimeTypes.APPLICATION_JSON);
            response.getWriter()
                    .write(jsonb.toJson(GetUserResponse.entityToDtoMapper().apply(user.get())));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

}
