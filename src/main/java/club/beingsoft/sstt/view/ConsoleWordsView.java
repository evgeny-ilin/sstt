package club.beingsoft.sstt.view;

import club.beingsoft.sstt.controller.WordsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ConsoleWordsView implements WordsView {
    private static final Logger log = LoggerFactory.getLogger(ConsoleWordsView.class);
    private final WordsController controller;

    public ConsoleWordsView(WordsController controller) {
        this.controller = controller;
    }

    @Override
    public void printWords() {
        Map<String, Integer> words = controller.getWords();
        log.info("Printing words");
        words.entrySet().forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
    }
}
