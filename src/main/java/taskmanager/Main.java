package taskmanager;

import taskmanager.controller.MainController;
import taskmanager.controller.Notifier;
import org.apache.log4j.Logger;


/**
 * Created by darkie on 30.01.17.
 */
public class Main {
    public static Logger logger = Logger.getLogger(Main.class);
    public static void main(String[] args) {
        logger.info("TaskManager application started");
        MainController mainController = new MainController();
        mainController.Init(mainController);
        mainController.updateView();

        Notifier notifier = new Notifier(mainController);
        notifier.run();
    }
}
