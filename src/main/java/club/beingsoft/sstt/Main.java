package club.beingsoft.sstt;

import club.beingsoft.sstt.controller.WordsController;
import club.beingsoft.sstt.model.WordsModel;
import club.beingsoft.sstt.view.ConsoleWordsView;
import club.beingsoft.sstt.view.WordsView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        WordsModel model = new WordsModel();
        WordsController controller = new WordsController(model);
        WordsView view = new ConsoleWordsView(controller);
        log.info("---------------------=> Start SimbirSoft TestTask <=---------------------");
        if (args.length == 0) {
            log.error("There is no url in command line parameter");
            System.exit(-1);
        }
        controller.setUrl(args[0]);
        view.printWords();
        log.info("---------------------=> End SimbirSoft TestTask <=---------------------");
    }
}
