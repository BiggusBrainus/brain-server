package at.htlkaindorf.bigbrain.server.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a question for the game. At the moment,
 * there is always exactly one correct answer string
 * and multiple incorrect answer strings.
 * @author m4ttm00ny
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private int qid;
    private String question;
    private Category category;
    private String correct;
    private List<String> wrong;
}
