package at.htlkaindorf.bigbrain.server.game;

import at.htlkaindorf.bigbrain.server.beans.*;
import at.htlkaindorf.bigbrain.server.db.access.QuestionsAccess;
import at.htlkaindorf.bigbrain.server.db.access.UsersAccess;
import at.htlkaindorf.bigbrain.server.errors.UnknownCategoryException;
import at.htlkaindorf.bigbrain.server.websockets.LobbyGameHandler;
import at.htlkaindorf.bigbrain.server.websockets.res.EndOfGameResponse;
import at.htlkaindorf.bigbrain.server.websockets.res.NextQuestionResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains the actual game logic. This is
 * a game that was started in a certain lobby.
 * @version BigBrain v1
 * @since 25.05.2021
 * @author m4ttm00ny
 */

public class Game {
    private Lobby lobby;
    private List<Question> questions;
    private List<Integer> submissions;
    private Map<User, List<Boolean>> scores = new HashMap<>();

    public Game(Lobby lobby) throws SQLException, ClassNotFoundException, UnknownCategoryException {
        this.lobby = lobby;
        QuestionsAccess acc = QuestionsAccess.getInstance();
        questions = acc.getRandomQuestionsFromCategories(5, lobby.getCategories().stream().map(Category::getCid).collect(Collectors.toList()));
        submissions = new ArrayList<>(Arrays.asList(new Integer[5]));
        Collections.fill(submissions, 0);
        lobby.getPlayers().forEach(p -> scores.put(p, new ArrayList<>(Arrays.asList(new Boolean[5]))));
    }

    public void startGame() {
        lobby.broadcast(new NextQuestionResponse(questions.get(0)));
    }

    public void submitAnswer(User player, int question, String answer) throws IOException {
        // has already submitted answer, just ignore ...
        if (scores.get(player).get(question) != null) return;
        scores.get(player).set(question, questions.get(question).getCorrect().equals(answer));
        submissions.set(question, submissions.get(question)+1);
        // compare with current number of players, because someone
        // might disconnect in the middle of the game ...
        if (submissions.get(question) >= lobby.getPlayers().size()) {
            if (question == questions.size()-1) {
                endGame();
            } else {
                lobby.broadcast(new NextQuestionResponse(questions.get(question+1)));
            }
        }
    }

    public void endGame() {
        List<Rank> ranking = scores.keySet().stream().map(u -> new Rank(u, scores.get(u).stream().filter(b -> b).count())).collect(Collectors.toList());
        try {
            UsersAccess acc = UsersAccess.getInstance();
            acc.addGame(ranking.get(0).getUser());
        } catch (SQLException|ClassNotFoundException e) {
        } finally {
            lobby.broadcast(new EndOfGameResponse(ranking));
        }
    }
}
