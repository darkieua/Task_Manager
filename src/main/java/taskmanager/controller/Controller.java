package main.taskmanager.controller;

import main.taskmanager.model.*;
import main.taskmanager.view.*;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by darkie on 22.01.17.
 */

public class Controller {
    private static String FILE_NAME = "list.bin";
    private MainFrame mainForm;
    private TaskList model;

    private Controller controller;
    private ToolbarController toolbar;
    private CalendarController calendar;
    private ListController list;
    private ConfirmExitDialogController confirmExit;

    protected static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

    public Controller () {

    }

    public void Init (Controller controller) {
        this.controller = controller;
        controller.setModel(controller.loadFromFile(controller.getFileName()));
        controller.toolbar = new ToolbarController(controller);
        controller.calendar = new CalendarController(controller);
        controller.list = new ListController(controller);
        controller.confirmExit = new ConfirmExitDialogController(controller);
        controller.createView();
    }

    public TaskList loadFromFile(String filename) {
        TaskList list = new ArrayTaskList();
        try {
            TaskIO.readBinary(list, new File(filename));
            model.setSaved(true);
        } catch (FileNotFoundException ex) {
            throwError("Autosave file not found. First run?");
        } finally {
            return list;
        }
    }


    //Метод инициализации главного диалогового окна
    public void createView () {
        controller.mainForm = new MainFrame();
        controller.mainForm.pack();
        controller.mainForm.setVisible(true);
        controller.initControllers();
    }

    private void initControllers() {
        if (getModel() == null) {
            System.out.println("Model is null");
        }
        controller.setExitOperationListener();
        controller.toolbar.setEditButtonListener();
        controller.toolbar.setRemoveButtonListener();
        controller.toolbar.setAddButtonListener();
        controller.toolbar.setLoadButtonListener();
        controller.toolbar.setSaveButtonListener();
        controller.calendar.createCalendar();
    }

    //Обновляет вид относительно актуальной модели
    public void updateView() {
        if (controller == null) {
            System.out.println("controller = null");
        }
        controller.mainForm.setModel(model);
        controller.list.updateTaskArea(mainForm);
        controller.toolbar.updateCombobox(mainForm.getTaskCombobox());
    }

    protected void throwError(String msg) {
        ErrorDialog errorForm = new ErrorDialog(this);
        errorForm.pack();
        errorForm.setErrorLabelText(msg);
        errorForm.setVisible(true);
    }

    protected void throwMessage(String title, String msg) {
        MessageDialog mesageForm = new MessageDialog(this, title, msg);
        mesageForm.pack();
        mesageForm.setVisible(true);
    }

    protected void setExitOperationListener() {
        controller.mainForm.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (model.isSaved()) {
                    closeApplication();
                }
                else {
                    confirmExit.throwConfirmExitDialog();
                    System.out.println("Tasklist is not saved");
                }
            }
        });
    }

    protected void closeApplication() {
        controller.mainForm.dispose();
        System.exit(0);
    }

    public String getFileName() {
        return FILE_NAME;
    }

    public void setModel(TaskList model) {
        try {
            controller.model = model;
        } catch (NullPointerException ex) {
            if (controller == null) {
                System.out.println("controller == null");
            }
            //ex.printStackTrace();
        }
    }

    public TaskList getModel() {
        return model;
    }

    public MainFrame getMainForm() {
        return mainForm;
    }
}
