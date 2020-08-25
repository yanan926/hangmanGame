package ca.cmpt213.a4.onlinehangman.controllers;

import ca.cmpt213.a4.onlinehangman.model.Game;
import ca.cmpt213.a4.onlinehangman.model.Message;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A controller used to handling all front end get/post requests
 *
 * @ authors: Yanan Liu, Student ID: 301368378, Email: yla568@sfu.ca
 */
@Controller
public class HangmanController {
    private Message promptMessage; //a resusable String object to display a prompt message at the screen
    private List<Game> gameList = new ArrayList<>();
    private AtomicLong nextId = new AtomicLong();
    private final String keyboard = "abcdefghijklmnopqrstuvwxyz";
    private final char[] letters = keyboard.toCharArray();

    private String getRandomWord() {
        File file = new File("src/commonWords.txt");
        List<String> result = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            while ((s = br.readLine()) != null) {
                result.add(s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Random r = new Random();
        int number = r.nextInt(result.size());
        return result.get(number);
    }

    //works like a constructor, but wait until dependency injection is done, so it's more like a setup
    @PostConstruct
    public void hangmanControllerInit() {
        promptMessage = new Message("Initializing...");
    }

    @GetMapping("/welcome")
    public String showWelcomePage(Model model) {
        promptMessage.setMessage("Hey there!");
        model.addAttribute("promptMessage", promptMessage);
        // take the user to game.html
        return "welcome";
    }


    @PostMapping("/game")
    @ResponseStatus(HttpStatus.CREATED)
    public String createNewGame(Model model) {
        Game newGame = new Game(getRandomWord());
        newGame.setId(nextId.incrementAndGet());
        gameList.add(newGame);
        model.addAttribute("game", newGame);
        String progress = new String(newGame.getCurrentAnswer()).replace("", " ").trim();
        model.addAttribute("progress", progress);
        model.addAttribute("letters", letters);
        return "game";
    }

    @PostMapping("/game/{id}")
    public String postGame(@PathVariable("id") long gameId, Model model, @RequestParam(value = "letter", required = false) char input) {
        for (Game game : gameList) {
            if (game.getId() == gameId) {
                if (game.getStatus().equals("Won")) {
                    return "won";
                }
                if (game.getStatus().equals("Lose")) {
                    return "lose";
                }
                model.addAttribute("letters", letters);
                game.setUserAnswer(input);
                game.setCurrentAnswer();
                game.judgeUserAnswer();
                game.addCurrentGuessNumber();
                game.setStatus();
                model.addAttribute("game", game);
                String progress = null;
                if (game.getCurrentWrongNumber() == 7) {
                    progress = game.getAnswer().replace("", " ").trim();
                } else {
                    progress = new String(game.getCurrentAnswer()).replace("", " ").trim();
                }
                model.addAttribute("progress", progress);
                model.addAttribute("letters", letters);
                return "game";
            }
        }
        throw new GameNotFoundException();
    }

    @GetMapping("/game/{id}")
    public String getGame(@PathVariable("id") long gameId, Model model) {
        for (Game game : gameList) {
            if (game.getId() == gameId) {
                if (game.getStatus().equals("Won")) {
                    return "won";
                }
                if (game.getStatus().equals("Lose")) {
                    return "lose";
                }
                model.addAttribute("letters", letters);
                model.addAttribute("game", game);
                String progress = new String(game.getCurrentAnswer()).replace("", " ").trim();
                model.addAttribute("progress", progress);
                model.addAttribute("letters", letters);
                return "game";
            }
        }
        throw new GameNotFoundException();
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(GameNotFoundException.class)
    public String badIdExceptionHandler() {
        return "404";
    }


}