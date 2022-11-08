package pl.edu.pg.eti.kask.rpg.social.network.controller;

import pl.edu.pg.eti.kask.rpg.social.network.dto.CreateComment;
import pl.edu.pg.eti.kask.rpg.social.network.entity.Comment;
import pl.edu.pg.eti.kask.rpg.social.network.entity.CommentType;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;
import pl.edu.pg.eti.kask.rpg.social.network.service.UserCommentService;
import pl.edu.pg.eti.kask.rpg.social.network.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("")
public class UserCommentController {

    /**
     * Service for user entity operations.
     */
    private UserService service;

    private UserCommentService commentService;


    public UserCommentController() {
    }

    @Inject
    public void setService(UserService service) {
        this.service = service;
    }

    @Inject
    public void setCommentService(UserCommentService commentService) {
        this.commentService = commentService;
    }

    @GET
    @Path("users/{userid}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserComments(@PathParam("userid") Integer id) {

        Optional<User> user = service.findById(id);
        if (user.isPresent()) {
            return Response
                    .ok(commentService.getUserComments(user.get()))
                    .build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @GET
    @Path("users/{userid}/comments/{commentid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserComment(@PathParam("userid") Integer userId, @PathParam("commentid") Integer commentId) {

        Optional<User> user = service.findById(userId);
        if (user.isPresent()) {
            Optional<Comment> comment = commentService.getUserComment(user.get(), commentId);
            if (comment.isPresent()) {
                return Response
                        .ok(comment.get())
                        .build();
            } else {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @PUT
    @Path("users/{userid}/comments/{commentid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserComment(@PathParam("userid") Integer userId, @PathParam("commentid") Integer commentId,CreateComment updateComment) {

        Optional<User> user = service.findById(userId);
        if (user.isPresent()) {
            Optional<Comment> comment = commentService.getUserComment(user.get(), commentId);
            if (comment.isPresent()) {

                Comment commentWillBeUpdated = CreateComment.dtoToEntityMapper().apply(updateComment, user.get());
                commentWillBeUpdated.setId(comment.get().getId());
                commentService.updateComment(commentWillBeUpdated);
                return Response
                        .ok(commentWillBeUpdated)
                        .build();
            } else {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @DELETE
    @Path("users/{userid}/comments/{commentid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUserComment(@PathParam("userid") Integer userId, @PathParam("commentid") Integer commentId) {

        Optional<User> user = service.findById(userId);
        if (user.isPresent()) {
            Optional<Comment> comment = commentService.getUserComment(user.get(), commentId);
            if (comment.isPresent()) {
                commentService.deleteComment(commentId);

                return Response
                        .accepted()
                        .build();
            } else {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @POST
    @Path("users/{userid}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response CreateUserComment(@PathParam("userid") Integer userId, CreateComment createComment) {

        Optional<User> user = service.findById(userId);
        if (user.isPresent()) {
            Comment comment = CreateComment.dtoToEntityMapper().apply(createComment, user.get());
            commentService.createCommentForUser(comment, user.get());
            return Response
                    .ok(comment)
                    .build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }



}
