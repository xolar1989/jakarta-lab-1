package pl.edu.pg.eti.kask.rpg.social.network.service;

import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.rpg.social.network.dto.GetUserResponse;
import pl.edu.pg.eti.kask.rpg.social.network.dto.GetUserTest;
import pl.edu.pg.eti.kask.rpg.social.network.entity.User;
import pl.edu.pg.eti.kask.rpg.social.network.repository.UserRepository;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.Part;
import javax.servlet.http.PushBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer for all business actions regarding user entity.
 */
@ApplicationScoped
@NoArgsConstructor//Empty constructor is required for creating proxy while CDI injection.
public class UserService {

    @Resource(name = "image.dir")
    private String imageDir;

    /**
     * Repository for user entity.
     */
    private UserRepository repository;

    /**
     * @param repository repository for character entity
     */
    @Inject
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * @param login existing username
     * @return container (can be empty) with user
     */
    public Optional<User> find(String login) {
        return repository.find(login);
    }

    public void delete(User user){
        repository.delete(user);
    }


    public List<GetUserResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(user -> GetUserResponse.entityToDtoMapper().apply(user))
                .collect(Collectors.toList());
    }

    public List<User> findAllUsers(){
        return repository.findAll();
    }

    public void updateUser(GetUserResponse infoToUpdate,Integer id){
        repository.findById(id)
                        .ifPresent(user -> {
                            User newUser = User.builder()
                                    .id(user.getId())
                                    .email(infoToUpdate.getEmail())
                                    .name(infoToUpdate.getName())
                                    .login(infoToUpdate.getLogin())
                                    .commentsIds(user.getCommentsIds())
                                    .password(user.getPassword())
                                    .build();
                            repository.update(newUser);
                        });
    }


    public Optional<User> findById(Integer id) {
        return repository.findById(id);
    }

    /**
     * Seeks for single user using login and password. Can be use in authentication module.
     *
     * @param login    user's login
     * @param password user's password (hash)
     * @return container (can be empty) with user
     */
    public Optional<User> find(String login, String password) {
        return repository.findByLoginAndPassword(login, password);
    }

    /**
     * Saves new user.
     *
     * @param user new user to be saved
     */
    public void create(User user) {
        repository.create(user);
    }

    public void deleteImage(User user) {
        Path path = Path.of(imageDir, user.getId().toString()+".png");
        try{
            Files.deleteIfExists(path);
        } catch (Exception ex){

        }
        user.setImage(new byte[0]);
        repository.update(user);
    }

    public void createImage(User user, Part image) {
        if (user != null && ! image.getName().isEmpty()) {
            Path path = Path.of(imageDir, user.getId()+".png");
            try {
                if (!Files.exists(path)) {
                    Files.createFile(path);
                    Files.write(path, image.getInputStream().readAllBytes(), StandardOpenOption.WRITE);
                    user.setImage(image.getInputStream().readAllBytes());
                    repository.update(user);
                }
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    public void updateImage(User user, Part image) {
        System.out.println(imageDir);
        if (user.getImage().length > 0) {
            deleteImage(user);
        }
        createImage(user, image);
    }

    public void saveImage(User user){
        Path path = Path.of(imageDir, user.getId()+".png");

        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
                Files.write(path, user.getImage(), StandardOpenOption.WRITE);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
