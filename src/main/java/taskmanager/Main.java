package main.taskmanager;

import main.taskmanager.controller.Controller;
import main.taskmanager.controller.Notifier;
import main.taskmanager.model.TaskList;

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
