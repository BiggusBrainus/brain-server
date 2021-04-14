package at.htlkaindorf.bigbrain.server.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public static Question fromResultSet(ResultSet rs, Category c) throws SQLException {
        List<String> wrong = new ArrayList<>();
        wrong.add(rs.getString("wrong1"));
        String wrong2 = rs.getString("wrong2");
        if (wrong2 != null) wrong.add(wrong2);
        String wrong3 = rs.getString("wrong3");
        if (wrong3 != null) wrong.add(wrong3);
        return new Question(rs.getInt("qid"), rs.getString("question"), c, rs.getString("correct"), wrong);
    }
}
