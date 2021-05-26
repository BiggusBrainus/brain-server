package at.htlkaindorf.bigbrain.server.game;

import at.htlkaindorf.bigbrain.server.beans.Category;
import at.htlkaindorf.bigbrain.server.beans.Lobby;
import at.htlkaindorf.bigbrain.server.beans.Question;
import at.htlkaindorf.bigbrain.server.beans.User;
import at.htlkaindorf.bigbrain.server.db.access.QuestionsAccess;
import at.htlkaindorf.bigbrain.server.errors.UnknownCategoryException;
import at.htlkaindorf.bigbrain.server.websockets.LobbyGameHandler;
import at.htlkaindorf.bigbrain.server.websockets.res.NextQuestionResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Game {
    private Lobby lobby;
    private List<Question> questions;
    private Map<User, List<Boolean>> scores = new HashMap<>();

    public Game(Lobby lobby) throws SQLException, ClassNotFoundException, UnknownCategoryException {
        this.lobby = lobby;
        QuestionsAccess acc = QuestionsAccess.getInstance();
        questions = acc.getRandomQuestionsFromCategories(5, lobby.getCategories().stream().map(Category::getCid).collect(Collectors.toList()));
        lobby.getPlayers().forEach(p -> scores.put(p, new ArrayList<>()));
        lobby.broadcast(new NextQuestionResponse(questions.get(0)));
    }

    public void submitAnswer(User player, int question, String answer) throws IOException {
        scores.get(player).set(question, questions.get(question).getCorrect().equals(answer));
        LobbyGameHandler.sendMessage(player.getLobby().getConnections().get(player), new NextQuestionResponse(questions.get(question+1)));
    }
}
