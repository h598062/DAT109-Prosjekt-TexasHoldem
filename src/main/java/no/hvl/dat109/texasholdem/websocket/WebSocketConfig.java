package no.hvl.dat109.texasholdem.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Konfigurasjon for WebSocket meldinger i lobbyen<br>
 * endpoint for å koble til: /lobby-ws<br>
 * meldinger som sendes til /lobby/*lobbyid* blir sendt til serveren og håndtert deretter i en controller<br>
 * meldinger som sendes til /lobby/status/*lobbyid* blir broadcastet til alle abonnenter på samme lobbyid<br>
 * meldinger som sendes til /spiller/*spillernavn* blir sendt til spilleren med navnet *spillernavn*
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// dette blir en slags subscribe endpoint
		// alle meldinger som sendes til /topic/* blir broadcastet til alle abonnenter på samme * topic
		config.enableSimpleBroker("/lobbystatus", "/spiller");

		// dette blir en slags publish endpoint
		// alle meldinger som sendes til /lobby/* blir sendt til serveren og håndtert deretter i en controller
		config.setApplicationDestinationPrefixes("/lobby/");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// dette er et endpoint for å koble til websocketen til STOMP
		registry.addEndpoint("/lobby-ws");
	}
}
