package taskmanager;

import taskmanager.controller.Controller;
import taskmanager.controller.Notifier;
import org.apache.log4j.Logger;


/**
 * Created by darkie on 30.01.17.
 */
public class Main {
    public static Logger logger = Logger.getLogger(Main.class);
    public static void main(String[] args) {
        logger.info("TaskManager application started");
        Controller controller = new Controller();
        controller.Init(controller);
        controller.updateView();

        Notifier notifier = new Notifier(controller);
        notifier.run();
    }
}
