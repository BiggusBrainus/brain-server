package at.htlkaindorf.bigbrain.server.rest.req;

import at.htlkaindorf.bigbrain.server.beans.Category;
import lombok.Data;

import java.util.List;

/**
 * @version BigBrain v1
 * @since 12.05.2021
 * @author m4ttm00ny
 */

@Data
public class GetLobbiesRequest {
    private String cids;
}
