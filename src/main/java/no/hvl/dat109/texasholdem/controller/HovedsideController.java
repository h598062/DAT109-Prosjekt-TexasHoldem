package no.hvl.dat109.texasholdem.controller;

import no.hvl.dat109.texasholdem.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HovedsideController {

    @Autowired
    private LobbyService lobbyService;

    /**
     * Controller som henter oss inn på hovedside
     * @param model For å sende ting videre
     * @return JSP Fil
     */
    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("lobbies", lobbyService.getLobbies());
        return "hovedside";
    }
}
