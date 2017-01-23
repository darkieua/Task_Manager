package ua.edu.sumdu.j2se.dmitry.controller;

import ua.edu.sumdu.j2se.dmitry.model.*;
import ua.edu.sumdu.j2se.dmitry.view.*;


import javax.swing.*;
import javax.tools.Tool;
import java.awt.event.*;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Created by darkie on 22.01.17.
 */

public class Controller {
    protected static String FILE_NAME = "list.bin";
    protected static MainDialog mainForm;
    protected static TaskList model;

    protected static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

    //Сеттер модели
    public static void setModel(TaskList model) {
        Controller.model = model;
    }

    public static TaskList getModel() {
        return model;
    }

    public static MainDialog getMainForm() {
        return mainForm;
    }

    public static void setMainForm(MainDialog mainForm) {
        Controller.mainForm = mainForm;
    }

    public static void main(String[] args) {
        TaskList a = new ArrayTaskList();
        setModel(a);
        createView();
        updateView();
    }

    private void initModel() {

    }

    //Метод инициализации главного диалогового окна
    private static void createView () {
        mainForm = new MainDialog();
        mainForm.pack();
        mainForm.setVisible(true);
        ToolbarController.setEditButtonListener();
        ToolbarController.setRemoveButtonListener();
        ToolbarController.setAddButtonListener();
        ToolbarController.setLoadButtonListener();
        ToolbarController.setSaveButtonListener();
        CalendarController.createCalendar();
    }

    //Обновляет вид относительно актуальной модели
    public static void updateView() {
        mainForm.setModel(model);
        ListController.updateTaskArea(mainForm);
        ToolbarController.updateCombobox(mainForm.getTaskCombobox());
    }

    protected static void throwError(String msg) {
        ErrorDialog errorForm = new ErrorDialog();
        errorForm.pack();
        errorForm.setErrorLabelText(msg);
        errorForm.setVisible(true);
    }
}
