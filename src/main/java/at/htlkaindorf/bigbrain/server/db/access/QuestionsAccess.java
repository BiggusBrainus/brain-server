package at.htlkaindorf.bigbrain.server.db.access;

import at.htlkaindorf.bigbrain.server.beans.Category;
import at.htlkaindorf.bigbrain.server.beans.Question;
import at.htlkaindorf.bigbrain.server.db.DB_Access;
import at.htlkaindorf.bigbrain.server.db.DB_Properties;
import at.htlkaindorf.bigbrain.server.errors.UnknownCategoryException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DB Access class for all operations using all
 * tables in the questions database.
 * @author m4ttm00ny
 */

public class QuestionsAccess extends DB_Access {
    private static QuestionsAccess theInstance = null;

    private final String GET_CATEGORY_BY_ID_QRY                 = "SELECT cid, title, lang FROM categories WHERE cid = ?";
    private final String GET_CATEGORIES_BY_TITLE_QRY            = "SELECT cid, title, lang FROM categories WHERE title = ?";
    private final String GET_CATEGORIES_BY_TITLE_LANG_QRY       = "SELECT cid, title, lang FROM categories WHERE title = ? AND lang = ?";
    private final String GET_CATEGORIES_BY_LANG_QRY             = "SELECT cid, title, lang FROM categories WHERE lang = ?";
    private final String INSERT_CATEGORY_QRY                    = "INSERT INTO categories (title, lang) VALUES (?, ?)";
    private final String GET_QUESTIONS_BY_QUESTION_QRY          = "SELECT qid, question, category, correct, wrong1, wrong2, wrong3 FROM questions WHERE question = ?";
    private final String GET_QUESTIONS_BY_QUESTION_CATEGORY_QRY = "SELECT qid, question, category, correct, wrong1, wrong2, wrong3 FROM questions WHERE question = ? AND category = ?";
    private final String GET_QUESTIONS_FROM_CATEGORY_QRY        = "SELECT qid, question, category, correct, wrong1, wrong2, wrong3 FROM questions WHERE category = ?";
    private final String GET_RANDOM_QUESTIONS_QRY               = "SELECT qid, question, category, correct, wrong1, wrong2, wrong3, RANDOM() \"rand\" FROM questions ORDER BY rand LIMIT ?";
    private final String GET_RANDOM_CATEGORY_QUESTIONS_QRY      = "SELECT qid, question, category, correct, wrong1, wrong2, wrong3, RANDOM() \"rand\" FROM questions WHERE category = ? ORDER BY rand LIMIT ?";
    private final String INSERT_QUESTION_QRY                    = "INSERT INTO questions (question, category, correct, wrong1, wrong2, wrong3) VALUES (?, ?, ?, ?, ?, ?)";

    private PreparedStatement getCategoryByIdStat                   = null;
    private PreparedStatement getCategoriesByTitleStat              = null;
    private PreparedStatement getCategoriesByTitleLangStat          = null;
    private PreparedStatement getCategoriesByLangStat               = null;
    private PreparedStatement insertCategoryStat                    = null;
    private PreparedStatement getQuestionsByQuestionStat            = null;
    private PreparedStatement getQuestionsByQuestionCategoryStat    = null;
    private PreparedStatement getQuestionsFromCategoryStat          = null;
    private PreparedStatement getRandomQuestionsStat                = null;
    private PreparedStatement getRandomCategoryQuestionsStat        = null;
    private PreparedStatement insertQuestionStat                    = null;

    public static QuestionsAccess getInstance() throws SQLException, ClassNotFoundException {
        if (theInstance == null) {
            theInstance = new QuestionsAccess();
        }
        return theInstance;
    }

    private QuestionsAccess() throws SQLException, ClassNotFoundException {
        loadProperties();
        connect();
    }

    private void loadProperties() {
        this.db_url = DB_Properties.getPropertyValue("questions_url");
        this.db_driver = DB_Properties.getPropertyValue("driver");
        this.db_user = DB_Properties.getPropertyValue("questions_username");
        this.db_pass = DB_Properties.getPropertyValue("questions_password");
    }

    public List<Category> getAllCategories() throws SQLException {
        Statement stat = db.getStatement();
        ResultSet rs = stat.executeQuery("SELECT * FROM categories");
        List<Category> categories = new ArrayList<>();
        while (rs.next()) categories.add(Category.fromResultSet(rs));
        return categories;
    }

    public Category getCategoryById(int cid) throws SQLException, UnknownCategoryException {
        if (getCategoryByIdStat == null) {
            getCategoryByIdStat = db.getConnection().prepareStatement(GET_CATEGORY_BY_ID_QRY);
        }
        getCategoryByIdStat.setInt(1, cid);
        ResultSet rs = getCategoryByIdStat.executeQuery();
        if (!rs.next()) throw new UnknownCategoryException(String.format("No category with id %d was found!", cid));
        return Category.fromResultSet(rs);
    }

    public List<Category> getCategoriesById(List<Integer> cids) {
        List<Category> res = new ArrayList<>();
        for (int cid: cids) {
            try {
                res.add(getCategoryById(cid));
            } catch (SQLException|UnknownCategoryException e) {
            }
        }
        return res;
    }

    public List<Category> getCategoriesByTitle(String title) throws SQLException {
        if (getCategoriesByTitleStat == null) {
            getCategoriesByTitleStat = db.getConnection().prepareStatement(GET_CATEGORIES_BY_TITLE_QRY);
        }
        getCategoriesByTitleStat.setString(1, title);
        ResultSet rs = getCategoriesByTitleStat.executeQuery();
        List<Category> categories = new ArrayList<>();
        while (rs.next()) categories.add(Category.fromResultSet(rs));
        return categories;
    }

    public List<Category> getCategoriesByTitleLanguage(String title, String lang) throws SQLException {
        if (getCategoriesByTitleLangStat == null) {
            getCategoriesByTitleLangStat = db.getConnection().prepareStatement(GET_CATEGORIES_BY_TITLE_LANG_QRY);
        }
        getCategoriesByTitleLangStat.setString(1, title);
        getCategoriesByTitleLangStat.setString(2, lang);
        ResultSet rs = getCategoriesByTitleLangStat.executeQuery();
        List<Category> categories = new ArrayList<>();
        while (rs.next()) categories.add(Category.fromResultSet(rs));
        return categories;
    }

    public List<Category> getCategoriesByLanguage(String lang) throws SQLException {
        if (getCategoriesByLangStat == null) {
            getCategoriesByLangStat = db.getConnection().prepareStatement(GET_CATEGORIES_BY_LANG_QRY);
        }
        getCategoriesByLangStat.setString(1, lang);
        ResultSet rs = getCategoriesByLangStat.executeQuery();
        List<Category> categories = new ArrayList<>();
        while (rs.next()) categories.add(Category.fromResultSet(rs));
        return categories;
    }

    public void insertCategory(Category c) throws SQLException {
        if (insertCategoryStat == null) {
            insertCategoryStat = db.getConnection().prepareStatement(INSERT_CATEGORY_QRY);
        }
        insertCategoryStat.setString(1, c.getTitle());
        insertCategoryStat.setString(2, c.getLang());
        insertCategoryStat.executeUpdate();
    }

    public List<Question> getQuestionsByQuestion(String question) throws SQLException, UnknownCategoryException {
        if (getQuestionsByQuestionStat == null) {
            getQuestionsByQuestionStat = db.getConnection().prepareStatement(GET_QUESTIONS_BY_QUESTION_QRY);
        }
        getQuestionsByQuestionStat.setString(1, question);
        ResultSet rs = getQuestionsByQuestionStat.executeQuery();
        List<Question> questions = new ArrayList<>();
        while (rs.next()) questions.add(Question.fromResultSet(rs, getCategoryById(rs.getInt("cid"))));
        return questions;
    }

    public List<Question> getQuestionsByQuestionCategory(String question, Category c) throws SQLException {
        if (getQuestionsByQuestionCategoryStat == null) {
            getQuestionsByQuestionCategoryStat = db.getConnection().prepareStatement(GET_QUESTIONS_BY_QUESTION_CATEGORY_QRY);
        }
        getQuestionsByQuestionCategoryStat.setString(1, question);
        getQuestionsByQuestionCategoryStat.setInt(2, c.getCid());
        ResultSet rs = getQuestionsByQuestionCategoryStat.executeQuery();
        List<Question> questions = new ArrayList<>();
        while (rs.next()) questions.add(Question.fromResultSet(rs, c));
        return questions;
    }

    public List<Question> getQuestionsFromCategory(int cid) throws SQLException, UnknownCategoryException {
        if (getQuestionsFromCategoryStat == null) {
            getQuestionsFromCategoryStat = db.getConnection().prepareStatement(GET_QUESTIONS_FROM_CATEGORY_QRY);
        }
        getQuestionsFromCategoryStat.setInt(1, cid);
        ResultSet rs = getQuestionsFromCategoryStat.executeQuery();
        List<Question> questions = new ArrayList<>();
        Category c = getCategoryById(cid);
        while (rs.next()) questions.add(Question.fromResultSet(rs, c));
        return questions;
    }

    public List<Question> getRandomQuestions(int amount) throws SQLException, UnknownCategoryException {
        if (getRandomQuestionsStat == null) {
            getRandomCategoryQuestionsStat = db.getConnection().prepareStatement(GET_RANDOM_QUESTIONS_QRY);
        }
        getRandomQuestionsStat.setInt(1, amount);
        ResultSet rs = getRandomQuestionsStat.executeQuery();
        List<Question> questions = new ArrayList<>();
        while (rs.next()) questions.add(Question.fromResultSet(rs, getCategoryById(rs.getInt("cid"))));
        return questions;
    }

    public List<Question> getRandomQuestionsFromCategory(int amount, int cid) throws UnknownCategoryException, SQLException {
        if (getRandomCategoryQuestionsStat == null) {
            getRandomCategoryQuestionsStat = db.getConnection().prepareStatement(GET_RANDOM_CATEGORY_QUESTIONS_QRY);
        }
        getRandomCategoryQuestionsStat.setInt(1, cid);
        getRandomCategoryQuestionsStat.setInt(2, amount);
        ResultSet rs = getRandomCategoryQuestionsStat.executeQuery();
        List<Question> questions = new ArrayList<>();
        Category c = getCategoryById(cid);
        while (rs.next()) questions.add(Question.fromResultSet(rs, c));
        return questions;
    }

    public void insertQuestion(Question question) throws SQLException {
        if (insertQuestionStat == null) {
            insertQuestionStat = db.getConnection().prepareStatement(INSERT_QUESTION_QRY);
        }
        insertQuestionStat.setString(1, question.getQuestion());
        insertQuestionStat.setInt(2, question.getCategory().getCid());
        insertQuestionStat.setString(3, question.getCorrect());
        for (int i = 0; i < 3; i++) {
            insertQuestionStat.setString(4+i, question.getWrong().size() > i ? question.getWrong().get(i) : null);
        }
        insertQuestionStat.executeUpdate();
    }
}
