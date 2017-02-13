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
    private static String FILE_NAME = "list.bin"; //Default name of save file
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

    //Method loads model from
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


    //Method creates main window of application
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

    //Updates view relatively to the model
    public void updateView() {
        mainController.mainForm.setModel(model);
        mainController.list.updateTaskArea(mainForm);
        mainController.toolbar.updateCombobox(mainForm.getTaskCombobox());
    }

    //Throws new error dialog window
    protected void throwError(String msg) {
        ErrorDialog errorForm = new ErrorDialog(this);
        errorForm.pack();
        errorForm.setErrorLabelText(msg);
        errorForm.setVisible(true);
    }

    //Throw new dialog window with some message/information
    protected void throwMessage(String title, String msg) {
        MessageDialog mesageForm = new MessageDialog(this, title, msg);
        mesageForm.pack();
        mesageForm.setVisible(true);
    }

    //Throws new exit confirmation dialog, if the model is not saved. If it is saved, closes the application ( closeApplication() ).
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

    //Closes the application
    protected void closeApplication () {
        mainController.mainForm.dispose();
        System.exit(0);
    }

    //Returns the name of save file
    public String getFileName() {
        return FILE_NAME;
    }

    //Sets the model (tasklist) referenece in controller
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

    //Adds new task to the model, referenced in controller
    public void addNewTask(Task newTask) {
        mainController.getModel().add(newTask);
        mainController.getModel().setSaved(false);
        toolbar.updateCombobox(mainController.getMainForm().getTaskCombobox());
        mainController.updateView();
    }

    //Returns the model (tasklist) referenced in controller
    public TaskList getModel() {
        return model;
    }

    //Returns the reference to the instance of main window of application
    public MainFrame getMainForm() {
        return mainForm;
    }
}
