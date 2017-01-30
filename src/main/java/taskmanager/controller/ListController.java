package main.taskmanager.controller;

import main.taskmanager.view.MainFrame;

/**
 * Created by darkie on 22.01.17.
 * Класс, отвечающий за поведение элементов, которые находятся
 * на вкладке List
 */
public class ListController extends Controller {
    private Controller controller;

    public ListController(Controller controller) {
        this.controller = controller;
    }

    //Метод обновляет текстовую область согласно текущей модели
    public void updateTaskArea(MainFrame form) {
        form.getTaskArea().setText(controller.getModel().toString());
    }

}
