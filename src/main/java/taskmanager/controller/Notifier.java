package taskmanager.controller;

import taskmanager.model.Task;

import java.util.Date;

/**
 * Created by darkie on 29.01.17.
 */
public class Notifier extends MainController implements Runnable {
    private MainController mainController;

    private static long checkInterval = 1000;
    private static boolean stop = false;

    private int MIN_CHECK = 1000; //Минимальное время, за которое возможна проверка

    public Notifier (MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public synchronized void run() {
        if (checkInterval >= MIN_CHECK)
        while (!stop) {
            for (Task task : mainController.getModel()) {
                checkTask(task);
            }

            try {
                wait(checkInterval);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }
    private void checkTask (Task task) {
        if (task.isActive()) {
            if (task.isRepeated()) {
                if (Math.abs(task.nextTimeAfter(new Date()).getTime() - System.currentTimeMillis()) < MIN_CHECK) {
                    logger.info( "Task \"" + task.toString() + "\" should be executed!");
                    mainController.throwMessage("\"" + task.getTitle() + "\" notification", "Task \"" + task.getTitle() + "\" should be executed!");
                };
            }
            else {
                if (Math.abs(task.getTime().getTime() - System.currentTimeMillis()) < MIN_CHECK) {
                    logger.info( "Task \"" + task.toString() + "\" should be executed!");
                    mainController.throwMessage("\"" + task.getTitle() + "\" notification", "Task \"" + task.getTitle() + "\" should be executed!");
                };
            }
        }
    }
}
