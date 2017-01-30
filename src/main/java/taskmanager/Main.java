package taskmanager;

import taskmanager.controller.Controller;
import taskmanager.controller.Notifier;

/**
 * Created by darkie on 30.01.17.
 */
public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.Init(controller);
        controller.updateView();

        Notifier notifier = new Notifier(controller);
        notifier.run();
    }
}
