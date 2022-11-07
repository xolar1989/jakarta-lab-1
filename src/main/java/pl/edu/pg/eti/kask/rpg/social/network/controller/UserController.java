package pl.edu.pg.eti.kask.rpg.social.network.controller;


import pl.edu.pg.eti.kask.rpg.social.network.dto.CreateUserRequest;
import pl.edu.pg.eti.kask.rpg.social.network.dto.GetUserResponse;
import pl.edu.pg.eti.kask.rpg.social.network.dto.GetUserTest;
import pl.edu.pg.eti.kask.rpg.social.network.dto.GetUsersResponse;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;
import pl.edu.pg.eti.kask.rpg.social.network.service.UserCommentService;
import pl.edu.pg.eti.kask.rpg.social.network.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("")
public class UserController {

    /**
     * Service for user entity operations.
     */
    private UserService service;

    private UserCommentService commentService;

    /**
     * JAX-RS requires no-args constructor.
     */
    public UserController() {
    }

    @Inject
    public void setService(UserService service) {
        this.service = service;
    }


    @GET
    @Path("/usersTest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersTest() {
        return Response
                .ok(service.findAllUsers().stream()
                        .map(user -> GetUserTest
                                .builder()
                                .id(user.getId())
                                .email(user.getEmail())
                                .name(user.getName())
                                .login(user.getLogin())
                                .build()
                        ).collect(Collectors.toList()))
                .build();
    }


    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        return Response
                .ok(GetUsersResponse.entityToDtoMapper().apply(service.findAllUsers()))
                .build();
    }

    @GET
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") Integer id) {
        Optional<User> user = service.findById(id);
        if (user.isPresent()) {
            return Response
                    .ok(GetUserResponse.entityToDtoMapper().apply(user.get()))
                    .build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @POST
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUser(CreateUserRequest request) {
        User user = CreateUserRequest
                .dtoToEntityMapper()
                .apply(request);
        service.create(user);
        return Response
                .accepted()
                .build();
    }

    @DELETE
    @Path("/users/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") Integer id) {
        Optional<User> user = service.findById(id);

        if (user.isPresent()){
            service.delete(user.get());
            return Response
                    .accepted()
                    .build();
        }
        else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @PUT
    @Path("/users/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putUser(@PathParam("id") Integer id,GetUserResponse request) {
        Optional<User> user = service.findById(id);

        if (user.isPresent()) {


            service.updateUser(request,user.get().getId());
            return Response
                    .noContent()
                    .build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

}
