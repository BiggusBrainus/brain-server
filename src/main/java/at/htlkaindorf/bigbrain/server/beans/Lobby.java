package at.htlkaindorf.bigbrain.server.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a game lobby. Multiple users can be
 * in a lobby as players. The lobby can have multiple
 * card categories selected. Cards will be picked from these.
 * @author m4ttm00ny
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lobby {
    private String name;
    private List<User> players;
    private List<Category> categories;
}
