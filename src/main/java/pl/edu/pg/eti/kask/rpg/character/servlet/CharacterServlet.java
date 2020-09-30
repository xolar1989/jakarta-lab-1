package pl.edu.pg.eti.kask.rpg.character.servlet;

import pl.edu.pg.eti.kask.rpg.character.dto.CreateCharacterRequest;
import pl.edu.pg.eti.kask.rpg.character.dto.GetCharacterResponse;
import pl.edu.pg.eti.kask.rpg.character.dto.GetCharactersResponse;
import pl.edu.pg.eti.kask.rpg.character.dto.UpdateCharacterRequest;
import pl.edu.pg.eti.kask.rpg.character.entity.Character;
import pl.edu.pg.eti.kask.rpg.character.service.CharacterService;
import pl.edu.pg.eti.kask.rpg.character.service.ProfessionService;
import pl.edu.pg.eti.kask.rpg.servlet.HttpHeaders;
import pl.edu.pg.eti.kask.rpg.servlet.ServletUtility;
import pl.edu.pg.eti.kask.rpg.servlet.UrlFactory;

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
 * Servlet for handling HTTP requests considering characters operations. Servlet API does not allow named path
 * parameters so wildcard is used.
 */
@WebServlet(urlPatterns = {
        CharacterServlet.Paths.CHARACTERS + "/*",
        CharacterServlet.Paths.USER_CHARACTERS + "/*"
})
public class CharacterServlet extends HttpServlet {

    /**
     * Service for managing characters.
     */
    private CharacterService characterService;

    /**
     * Service for managing professions.
     */
    private ProfessionService professionService;


    @Inject
    public CharacterServlet(CharacterService characterService, ProfessionService professionService) {
        this.characterService = characterService;
        this.professionService = professionService;
    }

    /**
     * JSON-B mapping object. According to open liberty documentation creating those is expensive. The JSON-B is only
     * one of many solutions. JSON strings can be build by hand {@link StringBuilder} or with JSON-P API. Both JSON-B
     * and JSON-P are part of Jakarta EE whereas JSON-B is newer standard.
     */
    private final Jsonb jsonb = JsonbBuilder.create();

    /**
     * Definition of paths supported by this servlet. Separate inner class provides composition for static fields.
     */
    public static class Paths {

        /**
         * All characters or specified character.
         */
        public static final String CHARACTERS = "/api/characters";

        /**
         * All characters belonging to the caller principal or specified character of the caller principal.
         */
        public static final String USER_CHARACTERS = "/api/user/characters";

    }

    /**
     * Definition of regular expression patterns supported by this servlet. Separate inner class provides composition
     * for static fields. Whereas servlet activation path can be compared to {@link Paths} the path info (denoted by
     * wildcard in paths) can be compared using regular expressions.
     */
    public static class Patterns {

        /**
         * All characters.
         */
        public static final String CHARACTERS = "^/?$";

        /**
         * Specified character.
         */
        public static final String CHARACTER = "^/[0-9]+/?$";

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.CHARACTERS.equals(servletPath)) {
            if (path.matches(Patterns.CHARACTER)) {
                getCharacter(request, response);
                return;
            } else if (path.matches(Patterns.CHARACTERS)) {
                getCharacters(request, response);
                return;
            }
        } else if (Paths.USER_CHARACTERS.equals(servletPath)) {
            if (path.matches(Patterns.CHARACTER)) {
                getUserCharacter(request, response);
                return;
            } else if (path.matches(Patterns.CHARACTERS)) {
                getUserCharacters(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        if (Paths.USER_CHARACTERS.equals(request.getServletPath())) {
            if (path.matches(Patterns.CHARACTERS)) {
                postUserCharacter(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        if (Paths.USER_CHARACTERS.equals(request.getServletPath())) {
            if (path.matches(Patterns.CHARACTER)) {
                putUserCharacter(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = ServletUtility.parseRequestPath(request);
        if (Paths.USER_CHARACTERS.equals(request.getServletPath())) {
            if (path.matches(Patterns.CHARACTER)) {
                deleteUserCharacter(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    /**
     * Sends single character or 404 error if not found.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if any input or output exception occurred
     */
    private void getCharacter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Parsed request path is valid with character pattern and can contain starting and ending '/'.
        Long id = Long.parseLong(ServletUtility.parseRequestPath(request).replaceAll("/", ""));
        Optional<Character> character = characterService.find(id);

        if (character.isPresent()) {
            response.getWriter()
                    .write(jsonb.toJson(GetCharacterResponse.entityToDtoMapper().apply(character.get())));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Sends all characters as JSON.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if an input or output exception occurred
     */
    private void getCharacters(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter()
                .write(jsonb.toJson(GetCharactersResponse.entityToDtoMapper().apply(characterService.findAll())));
    }

    /**
     * Sends single caller principal's character or 404 error if not found.  Caller principal should be stored in
     * session context.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if any input or output exception occurred
     */
    private void getUserCharacter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Parsed request path is valid with character pattern and can contain starting and ending '/'.
        Long id = Long.parseLong(ServletUtility.parseRequestPath(request).replaceAll("/", ""));
        Optional<Character> character = characterService.findForCallerPrincipal(id);

        if (character.isPresent()) {
            response.getWriter()
                    .write(jsonb.toJson(GetCharacterResponse.entityToDtoMapper().apply(character.get())));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Sends all call principal's characters as JSON. Caller principal should be stored in session context.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if an input or output exception occurred
     */
    private void getUserCharacters(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter()
                .write(jsonb.toJson(GetCharactersResponse.entityToDtoMapper()
                        .apply(characterService.findAllForCallerPrincipal())));
    }

    /**
     * Decodes JSON request and stores new character.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if an input or output exception occurred
     */
    private void postUserCharacter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CreateCharacterRequest requestBody = jsonb.fromJson(request.getInputStream(), CreateCharacterRequest.class);

        Character character = CreateCharacterRequest
                .dtoToEntityMapper(name -> professionService.find(name).orElse(null), () -> null)
                .apply(requestBody);

        try {
            characterService.createForCallerPrincipal(character);
            //When creating new resource, its location should be returned.
            response.addHeader(HttpHeaders.LOCATION,
                    UrlFactory.createUrl(request, Paths.USER_CHARACTERS, character.getId().toString()));
            //When creating new resource, appropriate code should be set.
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Decodes JSON request and updates existing character.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if an input or output exception occurred
     */
    private void putUserCharacter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Parsed request path is valid with character pattern and can contain starting and ending '/'.
        Long id = Long.parseLong(ServletUtility.parseRequestPath(request).replaceAll("/", ""));
        Optional<Character> character = characterService.findForCallerPrincipal(id);

        if (character.isPresent()) {
            UpdateCharacterRequest requestBody = jsonb.fromJson(request.getInputStream(),
                    UpdateCharacterRequest.class);

            UpdateCharacterRequest.dtoToEntityUpdater().apply(character.get(), requestBody);

            characterService.update(character.get());
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    /**
     * Deletes existing character denoted by path param.
     *
     * @param request  http request
     * @param response http response
     * @throws IOException if an input or output exception occurred
     */
    private void deleteUserCharacter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Parsed request path is valid with character pattern and can contain starting and ending '/'.
        Long id = Long.parseLong(ServletUtility.parseRequestPath(request).replaceAll("/", ""));
        Optional<Character> character = characterService.findForCallerPrincipal(id);

        if (character.isPresent()) {
            characterService.delete(character.get().getId());
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
