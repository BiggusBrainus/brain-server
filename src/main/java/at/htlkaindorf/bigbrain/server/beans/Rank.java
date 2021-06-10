package at.htlkaindorf.bigbrain.server.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rank {
    private User user;
    private Long score;
}
