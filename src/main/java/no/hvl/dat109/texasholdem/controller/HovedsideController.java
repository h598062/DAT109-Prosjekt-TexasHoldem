package no.hvl.dat109.texasholdem.controller;

import no.hvl.dat109.texasholdem.game.Lobby;
import no.hvl.dat109.texasholdem.game.Spiller;
import no.hvl.dat109.texasholdem.service.LobbyService;
import no.hvl.dat109.texasholdem.websocket.LobbyAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HovedsideController {
	Logger logger = LoggerFactory.getLogger(HovedsideController.class);

	@Autowired
	private final LobbyService lobbyService;

	public HovedsideController(LobbyService lobbyService) {
		this.lobbyService = lobbyService;
	}


	/**
	 * Controller som henter oss inn på hovedside
	 *
	 * @param model For å sende ting videre
	 *
	 * @return JSP Fil
	 */
	@GetMapping("/")
	public String getIndex(Model model) {
		model.addAttribute("lobbies", lobbyService.getLobbies());
		return "tmpMainpage";
	}

	/**
	 * POST endpoint for å forsøke å opprette en ny lobby
	 *
	 * @param lobbyId    String navn / id på lobby, må være unik
	 * @param lobbyLeder String navn / id på lobbyleder / den som oppretet lobbyen
	 * @param ra         For å sende data til jsp
	 *
	 * @return redirect til en annen endpoint
	 */
	@PostMapping("/createLobby")
	public String createLobby(@RequestParam("lobbyIdCreate") String lobbyId, @RequestParam String lobbyLeder,
	                          RedirectAttributes ra) {
		logger.info("Create lobby requested");
		List<String> errors = new ArrayList<>();
		if (lobbyId.isBlank()) {
			errors.add("Lobby id cannot be blank");
		}
		if (lobbyLeder.isBlank()) {
			errors.add("Lobby leader cannot be blank");
		}
		if (errors.isEmpty()) {
			try {
				lobbyService.createLobby(lobbyId, lobbyLeder);
			} catch (LobbyAlreadyExistsException e) {
				errors.add(e.getMessage());
			}
		}
		if (errors.isEmpty()) {
			ra.addFlashAttribute("lobbyId", lobbyId);
			ra.addFlashAttribute("spillerNavn", lobbyLeder);
			return "redirect:/lobby";
		} else {
			ra.addFlashAttribute("errors", errors);
			return "redirect:/";
		}
	}

	/**
	 * POST endpoint for å forsøke å opprette en ny lobby
	 *
	 * @param lobbyId     String navn / id på lobby en skal joine
	 * @param spillerNavn String navn / id på spilleren som skal joine, kan ikke allerede være i bruk i lobbyen
	 * @param ra          For å sende data til jsp
	 *
	 * @return redirect til en annen endpoint
	 */
	@PostMapping("/joinLobby")
	public String joinLobby(@RequestParam("lobbySelect") String lobbyId, @RequestParam String spillerNavn,
	                        RedirectAttributes ra) {
		logger.info("Join lobby requested");
		List<String> errors = new ArrayList<>();
		if (lobbyId.isBlank()) {
			errors.add("Lobby id cannot be blank");
		}
		if (spillerNavn.isBlank()) {
			errors.add("Spiller name cannot be blank");
		}
		if (errors.isEmpty()) {
			Lobby lobby = lobbyService.getLobby(lobbyId);
			if (lobby == null) {
				errors.add("Lobby does not exist");
			} else {
				Spiller nySpiller = new Spiller(spillerNavn);
				lobby.leggTilSpiller(nySpiller);
			}
		}
		if (errors.isEmpty()) {
			ra.addFlashAttribute("lobbyId", lobbyId);
			ra.addFlashAttribute("spillerNavn", spillerNavn);
			return "redirect:/lobby";
		} else {
			ra.addFlashAttribute("errors", errors);
			return "redirect:/";
		}
	}
}
