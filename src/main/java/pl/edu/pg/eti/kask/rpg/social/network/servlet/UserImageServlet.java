package pl.edu.pg.eti.kask.rpg.social.network.servlet;

import pl.edu.pg.eti.kask.rpg.servlet.MimeTypes;
import pl.edu.pg.eti.kask.rpg.servlet.ServletUtility;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;
import pl.edu.pg.eti.kask.rpg.social.network.service.UserService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = UserImageServlet.Paths.IMAGES + "/*")
@MultipartConfig(maxFileSize = 200 * 1024)
public class UserImageServlet extends HttpServlet {
    private UserService service;

    @Inject
    public UserImageServlet(UserService service) {
        this.service = service;
    }

    public static class Paths {
        public static final String IMAGES = "/api2/images";
    }

    public static class Patterns {
        public static final String USER = "^/[0-9]+/?$";
    }

    static class Parameters {
        static final String USER_IMAGE = "image";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.IMAGES.equals(servletPath)) {
            if (path.matches(Patterns.USER)) {
                getUser(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    private void getUser(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String stringId = ServletUtility.parseRequestPath(request).replaceAll("/", "");
        Integer id = Integer.valueOf(stringId);
        Optional<User> user = service.findById(id);
        if (user.isPresent()) {
            if(user.get().getImage().length == 0){
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            byte[] image = user.get().getImage();
            response.addHeader(HttpHeaders.CONTENT_TYPE, MimeTypes.IMAGE_PNG);
            response.setContentLength(image.length);
            response.getOutputStream().write(image);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = ServletUtility.parseRequestPath(request);
        String servletPath = request.getServletPath();

        if (Paths.IMAGES.equals(servletPath) && path.matches(Patterns.USER)) {
            deleteUser(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String stringId = ServletUtility.parseRequestPath(request).replaceAll("/", "");
        Integer id = Integer.valueOf(stringId);
        Optional<User> user = service.findById(id);
        if(user.isPresent()) {
            System.out.println(user.get().getId());
            service.deleteImage(user.get());
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = ServletUtility.parseRequestPath(request);
        String servletPath = request.getServletPath();

        if (Paths.IMAGES.equals(servletPath) && path.matches(Patterns.USER)) {
            putImage(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void putImage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String stringId = ServletUtility.parseRequestPath(request).replaceAll("/", "");
        Integer id = Integer.valueOf(stringId);
        Optional<User> user = service.findById(id);

        if (user.isPresent()) {
            Part image = request.getPart(Parameters.USER_IMAGE);
            if(image != null){
                service.updateImage(user.get(), image);
            }
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }


}
