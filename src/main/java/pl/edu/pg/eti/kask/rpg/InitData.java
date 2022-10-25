package pl.edu.pg.eti.kask.rpg;

import lombok.SneakyThrows;
import pl.edu.pg.eti.kask.rpg.social.network.entity.Comment;
import pl.edu.pg.eti.kask.rpg.social.network.entity.CommentType;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;
import pl.edu.pg.eti.kask.rpg.social.network.service.UserCommentService;
import pl.edu.pg.eti.kask.rpg.social.network.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.InputStream;

@ApplicationScoped
public class InitData {
    private final UserService userService;

    private final UserCommentService userCommentService;

    @Inject
    InitData(UserService userService, UserCommentService userCommentService) {
        this.userService = userService;
        this.userCommentService = userCommentService;
    }

    public void contextInitialized(@Observes @Initialized(ApplicationScoped.class) Object init) {
        init();
    }


    private synchronized void init(){
        User user1 = User.builder()
                .login("user1")
                .email("email@dd")
                .name("michal")
                .image(getResourceAsByteArray("configuration/avatar/zereni.png"))
                .build();

        Comment comment1 = Comment.builder()
                .content("fajny komentarz")
                .type(CommentType.HATE_COMMENT)
                .build();


        Comment comment2 = Comment.builder()
                .content("lorem ipsum")
                .type(CommentType.NORMAL_COMMENT)
                .build();


        Comment comment3 = Comment.builder()
                .content("eikdmkmksdlsmdksd")
                .type(CommentType.HATE_COMMENT)
                .build();

        User user2 = User.builder()
                .login("user2")
                .email("email@dd2")
                .name("carlos")
                .image(getResourceAsByteArray("configuration/avatar/uhlbrecht.png"))
                .build();


        Comment comment5 = Comment.builder()
                .content("fajny komentarz user 2")
                .build();


        Comment comment6 = Comment.builder()
                .content("lorem ipsum user2 ")
                .build();





        User user3 = User.builder()
                .login("user3")
                .email("email@dd3")
                .name("andrew")
                .image(getResourceAsByteArray("configuration/avatar/calvian.png"))
                .build();

        User user4 = User.builder()
                .login("login4")
                .email("email@d4")
                .name("drake")
                .image(getResourceAsByteArray("configuration/avatar/eloise.png"))
                .build();

        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
        userService.create(user4);

        userCommentService.createCommentForUser(comment1,user1);
        userCommentService.createCommentForUser(comment2,user1);
        userCommentService.createCommentForUser(comment3,user1);


        userCommentService.createCommentForUser(comment5,user2);
        userCommentService.createCommentForUser(comment6,user2);

        userService.saveImage(user1);
        userService.saveImage(user2);
        userService.saveImage(user3);
        userService.saveImage(user4);

    }

    @SneakyThrows
    private byte[] getResourceAsByteArray(String name) {
        try (InputStream is = this.getClass().getResourceAsStream(name)) {
            return is.readAllBytes();
        }
    }

}