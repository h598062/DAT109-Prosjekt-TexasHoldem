package no.hvl.dat109.texasholdem.service;

import jakarta.persistence.Lob;
import no.hvl.dat109.texasholdem.game.Lobby;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LobbyService {
    private List<Lobby> lobbies = new ArrayList<>();


    /**
     * Lager en lobby og legger den til i listen med alle lobbies
     * @return Lobby
     */
    public Lobby createLobby() {
        Lobby lobby = new Lobby();
        lobbies.add(lobby);
        return lobby;
    }

    /**
     * Fjerner en lobby
     * @param lobby
     */
    public void removeLobby(Lobby lobby) {
        lobbies.remove(lobby);
    }

    public List<Lobby> getLobbies() {
        return lobbies;
    }
}
