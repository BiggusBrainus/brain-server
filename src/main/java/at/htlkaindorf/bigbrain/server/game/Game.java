package at.htlkaindorf.bigbrain.server.game;

import at.htlkaindorf.bigbrain.server.beans.Category;
import at.htlkaindorf.bigbrain.server.beans.Lobby;
import at.htlkaindorf.bigbrain.server.beans.Question;
import at.htlkaindorf.bigbrain.server.beans.User;
import at.htlkaindorf.bigbrain.server.db.access.QuestionsAccess;
import at.htlkaindorf.bigbrain.server.errors.UnknownCategoryException;
import at.htlkaindorf.bigbrain.server.websockets.LobbyGameHandler;
import at.htlkaindorf.bigbrain.server.websockets.res.EndOfGameResponse;
import at.htlkaindorf.bigbrain.server.websockets.res.NextQuestionResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

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
        lobby.getPlayers().forEach(p -> scores.put(p, new ArrayList<>()));
    }

    public void startGame() {
        lobby.broadcast(new NextQuestionResponse(questions.get(0)));
    }

    public void submitAnswer(User player, int question, String answer) throws IOException {
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
        List<AbstractMap.SimpleEntry<User, Long>> ranking = scores.keySet().stream().map(u -> new AbstractMap.SimpleEntry<>(u, scores.get(u).stream().filter(b -> b).count())).collect(Collectors.toList());
        lobby.broadcast(new EndOfGameResponse(ranking));
    }
}
