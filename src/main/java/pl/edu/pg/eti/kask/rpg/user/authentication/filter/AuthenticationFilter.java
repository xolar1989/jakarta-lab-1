package pl.edu.pg.eti.kask.rpg.user.authentication.filter;

import pl.edu.pg.eti.kask.rpg.character.servlet.CharacterServlet;
import pl.edu.pg.eti.kask.rpg.character.servlet.PortraitServlet;
import pl.edu.pg.eti.kask.rpg.servlet.AuthMethods;
import pl.edu.pg.eti.kask.rpg.servlet.HttpHeaders;
import pl.edu.pg.eti.kask.rpg.user.authentication.service.AuthenticationService;
import pl.edu.pg.eti.kask.rpg.user.context.UserContext;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

/**
 * Web filter with authentication mechanism. It should be configured to filter any servlet request that requires
 * authentication. In more complex application Security framework would be used. This is an example how filters can be
 * used. It uses BASIC authentication mechanism based on "Authorization" HTTP header.
 */
@WebFilter(urlPatterns = {
        PortraitServlet.Paths.PORTRAITS + "/*",
        CharacterServlet.Paths.CHARACTERS + "/*",
        CharacterServlet.Paths.USER_CHARACTERS + "/*"
})
public class AuthenticationFilter extends HttpFilter {

    /**
     * Service for authentication methods.
     */
    private AuthenticationService service;

    /**
     * User session context.
     */
    private UserContext context;

    @Inject
    public AuthenticationFilter(AuthenticationService service, UserContext context) {
        this.service = service;
        this.context = context;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (context.getPrincipal() == null) {
            //Check for authorization header with basic auth method
            String basic = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (basic == null || !basic.startsWith(AuthMethods.BASIC)) {
                response.setHeader(HttpHeaders.WWW_AUTHENTICATE, String.format(AuthMethods.BASIC_REALM, "Simple RPG"));
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            //Parse credentials
            basic = basic.replace(AuthMethods.BASIC, "").trim();
            basic = new String(Base64.getDecoder().decode(basic));
            String[] credentials = basic.split(":");

            //Check credentials
            if (!service.authenticate(credentials[0], credentials[1])) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
