package ua.edu.sumdu.j2se.dmitry;

import ua.edu.sumdu.j2se.dmitry.controller.Controller;
import ua.edu.sumdu.j2se.dmitry.controller.Notifier;
import ua.edu.sumdu.j2se.dmitry.model.TaskList;

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
