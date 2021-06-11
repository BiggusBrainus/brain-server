package at.htlkaindorf.bigbrain.server.websockets;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * The WebSocketConfig assigning each WebSocketHandler
 * to their listener endpoint.
 * @version BigBrain v1
 * @since 07.04.2021
 * @author m4ttm00ny
 */

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(new HelloHandler(), "/ws/hello").setAllowedOriginPatterns("*");
        webSocketHandlerRegistry.addHandler(new LobbyGameHandler(), "/ws/game").setAllowedOriginPatterns("*");
    }
}
