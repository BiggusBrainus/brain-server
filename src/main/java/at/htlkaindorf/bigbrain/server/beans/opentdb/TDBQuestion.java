package at.htlkaindorf.bigbrain.server.beans.opentdb;

import at.htlkaindorf.bigbrain.server.json.TDBQuestionQuestionDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a question as returned
 * from the TDB API.
 * @version BigBrain v1
 * @since 05.05.2021
 * @author m4ttm00ny
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TDBQuestion {
    private String category;
    private String type;
    private String difficulty;
    @JsonDeserialize(using = TDBQuestionQuestionDeserializer.class)
    private String question;
    @JsonDeserialize(using = TDBQuestionQuestionDeserializer.class)
    private String correct_answer;
    private List<String> incorrect_answers;
}
