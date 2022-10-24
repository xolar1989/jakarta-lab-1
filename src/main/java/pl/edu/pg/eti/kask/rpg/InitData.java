package pl.edu.pg.eti.kask.rpg;

import lombok.SneakyThrows;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;
import pl.edu.pg.eti.kask.rpg.social.network.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.InputStream;

@ApplicationScoped
public class InitData {
    private final UserService userService;

    @Inject
    public InitData(UserService userService) {
        this.userService = userService;
    }

    public void contextInitialized(@Observes @Initialized(ApplicationScoped.class) Object init) {
        init();
    }


    private synchronized void init(){
        User user1 = User.builder()
                .login("user1")
                .email("email@dd")
                .image(getResourceAsByteArray("configuration/avatar/zereni.png"))
                .build();

        User user2 = User.builder()
                .login("user2")
                .email("email@dd2")
                .image(getResourceAsByteArray("configuration/avatar/uhlbrecht.png"))
                .build();

        User user3 = User.builder()
                .login("user3")
                .email("email@dd3")
                .image(getResourceAsByteArray("configuration/avatar/calvian.png"))
                .build();

        User user4 = User.builder()
                .login("login4")
                .email("email@d4")
                .image(getResourceAsByteArray("configuration/avatar/eloise.png"))
                .build();

        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
        userService.create(user4);


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