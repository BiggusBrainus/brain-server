package at.htlkaindorf.bigbrain.server.beans.opentdb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a reply from the TDB
 * API service.
 * @version BigBrain v1
 * @since 05.05.2021
 * @author m4ttm00ny
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    private int response_code;
    private List<TDBQuestion> results;
}
