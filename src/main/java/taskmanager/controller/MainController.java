package taskmanager.controller;

import org.apache.log4j.Logger;
import taskmanager.model.*;
import taskmanager.view.*;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

/**
 * Created by darkie on 22.01.17.
 */

public class MainController {
    protected static Logger logger = Logger.getLogger(MainController.class);
    private static String FILE_NAME = "list.bin";
    private MainFrame mainForm;
    private TaskList model;

    private MainController mainController;
    private ToolbarController toolbar;
    private CalendarController calendar;
    private ListController list;
    private ConfirmExitDialogController confirmExit;

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");

    public void Init (MainController mainController) {
        this.mainController = mainController;
        mainController.setModel(mainController.loadFromFile(new File(mainController.getFileName())));
        mainController.toolbar = new ToolbarController(mainController);
        mainController.calendar = new CalendarController(mainController);
        mainController.list = new ListController(mainController);
        mainController.confirmExit = new ConfirmExitDialogController(mainController);
        mainController.createView();
    }

    public TaskList loadFromFile(File file) {
        TaskList list = new ArrayTaskList();
        try {
            TaskIO.readBinary(list, file);
            model.setSaved(true);
        } catch (FileNotFoundException ex) {
            logger.error("File not found");
        } finally {
            logger.info("Task list is being autoloaded from file");
            return list;
        }
    }


    //Метод инициализации главного диалогового окна
    public void createView () {
        mainController.mainForm = new MainFrame();
        mainController.mainForm.pack();
        mainController.mainForm.setVisible(true);
        mainController.initControllers();
    }

    private void initControllers() {
        mainController.setExitOperationListener();
        mainController.toolbar.setEditButtonListener();
        mainController.toolbar.setRemoveButtonListener();
        mainController.toolbar.setAddButtonListener();
        mainController.toolbar.setLoadButtonListener();
        mainController.toolbar.setSaveButtonListener();
        mainController.calendar.createCalendar();
    }

    //Обновляет вид относительно актуальной модели
    public void updateView() {
        mainController.mainForm.setModel(model);
        mainController.list.updateTaskArea(mainForm);
        mainController.toolbar.updateCombobox(mainForm.getTaskCombobox());
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
        mainController.mainForm.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (model.isSaved()) {
                    closeApplication();
                }
                else {
                    confirmExit.throwConfirmExitDialog();
                }
            }
        });
    }

    protected void closeApplication () {
        mainController.mainForm.dispose();
        System.exit(0);
    }
    public String getFileName() {
        return FILE_NAME;
    }

    public void setModel(TaskList model) {
        try {
            mainController.model = model;
        } catch (NullPointerException ex) {
            if (mainController == null) {
                System.out.println("mainController == null");
            }
            //ex.printStackTrace();
        }
    }

    public void addNewTask(Task newTask) {
        mainController.getModel().add(newTask);
        mainController.getModel().setSaved(false);
        toolbar.updateCombobox(mainController.getMainForm().getTaskCombobox());
        mainController.updateView();
    }

    public TaskList getModel() {
        return model;
    }

    public MainFrame getMainForm() {
        return mainForm;
    }
}
