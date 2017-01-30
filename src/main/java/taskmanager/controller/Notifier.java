package main.taskmanager.controller;

import main.taskmanager.model.Task;
import main.taskmanager.model.TaskList;
import main.taskmanager.view.MessageDialog;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by darkie on 29.01.17.
 */
public class Notifier extends Controller implements Runnable {
    private Controller controller;

    private static long checkInterval = 1000;
    private static boolean stop = false;

    private int MIN_CHECK = 1000; //Минимальное время, за которое возможна проверка
    private int MIN_NOTIFY = 1000; //Минимальное время, за которое возможно оповещение

    public Notifier (Controller controller) {
        this.controller = controller;
    }

    @Override
    public synchronized void run() {
        if (checkInterval >= MIN_CHECK)
        while (!stop) {
            for (Task task : controller.getModel()) {
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
        long timeleft = 0;
        if (task.isActive()) {
            if (!isFinished(task)) {
                timeleft = task.nextTimeAfter(new Date()).getTime() - System.currentTimeMillis();
                System.out.println("Timeleft: " + timeleft + ", " + (int)(Math.round( timeleft / 1000.0) * 1000));
            }
            else {
                controller.throwMessage("\"" + task.getTitle() + "\" notification", "Task \"" + task.getTitle() + "\" should be executed!");
                System.out.println("Task " + task.getTitle() + " is finished");
            }
        }
    }

    private boolean isFinished(Task task) {
            if ((System.currentTimeMillis() > task.getTime().getTime() && !task.isRepeated()) || (System.currentTimeMillis() > task.getEndTime().getTime() && task.isRepeated())) {
                System.out.println("Task " + task.getTitle() + " is finished, making unactive");
                task.setActive(false);
                controller.updateView();
                return true;
            } else return false;
    }
}
