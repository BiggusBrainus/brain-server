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
 * @version BigBrain v1
 * @since 07.04.2021
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

    public Question(String question, Category category, String correct, List<String> wrong) {
        this.question = question;
        this.category = category;
        this.correct = correct;
        this.wrong = wrong;
    }

    /**
     * Construct a new Question object from the ResultSet returned
     * from a Postgres query.
     * @param rs    The ResultSet object.
     * @param c     The Category the question falls into.
     * @return A Question object containing the ResultSet's info.
     * @throws SQLException     The ResultSet seems to be missing columns.
     */
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
