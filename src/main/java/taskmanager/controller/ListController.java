package taskmanager.controller;

import taskmanager.view.MainFrame;

/**
 * Created by darkie on 22.01.17.
 * Класс, отвечающий за поведение элементов, которые находятся
 * на вкладке List
 */
public class ListController extends MainController {
    private MainController mainController;

    public ListController(MainController mainController) {
        this.mainController = mainController;
    }

    //Метод обновляет текстовую область согласно текущей модели
    public void updateTaskArea(MainFrame form) {
        form.getTaskArea().setText(mainController.getModel().toString());
    }

}
