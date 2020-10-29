package club.beingsoft.sstt.controller;

import club.beingsoft.sstt.model.WordsModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class WordsController {
    private static final Logger log = LoggerFactory.getLogger(WordsController.class);
    private final WordsModel model;
    private URL url;

    public WordsController(WordsModel model) {
        this.model = model;
    }

    public Map<String, Integer> getWords() {
        return model.countWords();
    }

    public void setUrl(String argValue) {
        try {
            this.url = new URL(argValue);
            model.prepare(this.url);
        } catch (MalformedURLException e) {
            log.error("Couldn't get url: ", e);
            System.exit(-1);
        }
    }

}
