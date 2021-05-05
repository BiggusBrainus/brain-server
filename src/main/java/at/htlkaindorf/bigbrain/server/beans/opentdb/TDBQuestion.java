package at.htlkaindorf.bigbrain.server.beans.opentdb;

import at.htlkaindorf.bigbrain.server.json.TDBQuestionQuestionDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
