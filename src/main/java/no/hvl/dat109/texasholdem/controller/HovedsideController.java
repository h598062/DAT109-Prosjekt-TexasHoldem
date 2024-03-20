package no.hvl.dat109.texasholdem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HovedsideController {

    @GetMapping("/")
    public String getIndex() {
        return "hovedside";
    }
}
