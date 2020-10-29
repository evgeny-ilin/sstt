package club.beingsoft.sstt;

import club.beingsoft.sstt.controller.WordsController;
import club.beingsoft.sstt.model.WordsModel;
import club.beingsoft.sstt.view.ConsoleWordsView;
import club.beingsoft.sstt.view.WordsView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SsttRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(SsttRunner.class);

    @Autowired
    private WordsModel model;

    @Override
    public void run(String... args) {
        try {
            log.info("---------------------=> Start SimbirSoft TestTask <=---------------------");
            WordsController controller = new WordsController(model);
            WordsView view = new ConsoleWordsView(controller);
            if (args.length == 0) {
                log.error("There is no url in command line parameter");
                System.exit(-1);
            }
            controller.setUrl(args[0]);
            view.printWords();
            log.info("---------------------=> End SimbirSoft TestTask <=---------------------");
            System.out.println("Press enter to continue");
            System.in.read();
            System.exit(0);
        } catch (Exception e) {
            log.error("Something went wrong", e);
        }

    }
}
