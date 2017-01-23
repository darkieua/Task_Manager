package ua.edu.sumdu.j2se.dmitry.controller;

import ua.edu.sumdu.j2se.dmitry.view.MainDialog;

/**
 * Created by darkie on 22.01.17.
 * Класс, отвечающий за поведение элементов, которые находятся
 * на вкладке List
 */
public class ListController extends Controller {

    //Метод обновляет текстовую область согласно текущей модели
    public static void updateTaskArea(MainDialog form) {
        form.getTaskArea().setText(model.toString());
    }

}
