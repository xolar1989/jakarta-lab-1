package pl.edu.pg.eti.kask.rpg.social.network.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class GetUserTest {

    private Integer id;

    /**
     * User's login.
     */
    private String login;

    /**
     * Users's given name.
     */
    private String name;


    /**
     * User's contact email.
     */
    private String email;

    private List<Integer> commentsIds;

}
