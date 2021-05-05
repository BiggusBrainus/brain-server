package at.htlkaindorf.bigbrain.server.util;

import at.htlkaindorf.bigbrain.server.beans.Category;
import at.htlkaindorf.bigbrain.server.beans.Question;
import at.htlkaindorf.bigbrain.server.beans.opentdb.Reply;
import at.htlkaindorf.bigbrain.server.beans.opentdb.TDBQuestion;
import at.htlkaindorf.bigbrain.server.db.access.QuestionsAccess;
import at.htlkaindorf.bigbrain.server.db.access.UsersAccess;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class DBFiller {

    private static final String API_URL = "https://opentdb.com/api.php?amount=%d";
    private static final JsonMapper mapper = new JsonMapper();
    private static QuestionsAccess access;

    private static Reply getQuestions(int amount) throws IOException {
        return mapper.readValue(new URL(String.format(API_URL, amount)), Reply.class);
    }

    private static Question tdbToQuestion(TDBQuestion question) throws SQLException {
        List<Category> categories = access.getCategoriesByTitleLanguage(question.getCategory(), "en");
        Category c;
        if (categories.isEmpty()) {
            access.insertCategory(new Category(question.getCategory(), "en"));
            c = access.getCategoriesByTitleLanguage(question.getCategory(), "en").get(0);
        } else {
            c = categories.get(0);
        }
        List<Question> questions = access.getQuestionsByQuestionCategory(question.getQuestion(), c);
        Question q;
        if (questions.isEmpty()) {
            access.insertQuestion(new Question(question.getQuestion(), c, question.getCorrect_answer(), question.getIncorrect_answers().stream().map(s -> StringEscapeUtils.unescapeHtml4(s)).collect(Collectors.toList())));
            q = access.getQuestionsByQuestionCategory(question.getQuestion(), c).get(0);
        } else {
            q = questions.get(0);
        }
        return q;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        access = QuestionsAccess.getInstance();
        Reply reply = getQuestions(50);
        System.out.println("[*] Transforming TDBQuestions to Questions while inserting into DB ... ");
        for (TDBQuestion q: reply.getResults()) {
            System.out.println(q);
            System.out.println(tdbToQuestion(q));
        }
        System.out.println(String.format("[+] Done! Inserted ~%d questions!", reply.getResults().size()));
    }
}
