package at.htlkaindorf.bigbrain.server.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Useful for storing user-specific
 * ranking information - either in the
 * context of a game result or a global
 * overall ranking.
 * @version BigBrain v1
 * @since 10.06.2021
 * @author m4ttm00ny
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rank {
    private User user;
    private Long score;
}
