package at.htlkaindorf.bigbrain.server.websockets;

/**
 * All possible actions the LobbyGameHandler
 * sends / receives.
 * @version BigBrain v1
 * @since 25.05.2021
 * @author m4ttm00ny
 */

public enum LobbyGameHandlerActions {
    CONNECT_TO_LOBBY,
    LOBBY_PLAYERS_UPDATE,
    START_GAME,
    NEXT_QUESTION,
    ANSWER,
    END_OF_GAME
}
