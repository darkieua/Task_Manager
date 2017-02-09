package taskmanager.controller;

import taskmanager.Main;
import taskmanager.model.Task;
import taskmanager.model.TaskIO;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;

/**
 * Created by darkie on 22.01.17.
 * Класс, отвечающий за поведение элементов панели инструментов (выпадающий список задач и кнопки),
 * которая находится на форме вне зависимости от выбраной вкладки
 */
public class ToolbarController extends MainController {



    private MainController mainController;
    private EditController edit;

    public ToolbarController (MainController mainController) {
        this.mainController = mainController;
        this.edit = new EditController(mainController);
    }

    //Метод вешает слушатель на кнопку редактирования
    protected void setEditButtonListener() {
        mainController.getMainForm().getButtonEdit().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (mainController.getModel().size() > 0) {
                    edit.throwEditDialog(mainController.getModel().getTask(mainController.getMainForm().getTaskCombobox().getSelectedIndex()), mainController.getMainForm().getTaskCombobox().getSelectedIndex());
                    mainController.getModel().setSaved(false);
                    logger.info("Task \"" + mainController.getModel().getTask(mainController.getMainForm().getTaskCombobox().getSelectedIndex()).toString() + "\" is being edited");
                }
                else {
                    logger.error("Tried to edit a task from empty task list");
                    throwError("Task list is empty");
                }
            }
        });
    }

    //Метод вешает слушатель на кнопку удаления задачи
    protected void setRemoveButtonListener() {
        mainController.getMainForm().getRemoveButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (mainController.getModel().size() > 0) {
                    logger.info("Task \"" + mainController.getModel().getTask(mainController.getMainForm().getTaskCombobox().getSelectedIndex()).toString() + "\" was removed");
                    mainController.getModel().remove(mainController.getModel().getTask(mainController.getMainForm().getTaskCombobox().getSelectedIndex()));
                    mainController.getModel().setSaved(false);
                    updateCombobox(mainController.getMainForm().getTaskCombobox());
                    mainController.updateView();
                }
                else {
                    logger.error("Tried to remove task from empty task list");
                    throwError("Cannot remove, task list is empty already");
                }
            }
        });
    }

    protected void setAddButtonListener() {
        mainController.getMainForm().getAddButon().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Task newTask = new Task("Title", new Date());
                newTask.setActive(true);
                edit.throwEditDialog(newTask, 0);
            }
        });
    }

    protected void setLoadButtonListener() {
        mainController.getMainForm().getLoadButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String defaultPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath(); //Path of Main class, used as a default in filechooser
                FileNameExtensionFilter filter = new FileNameExtensionFilter("(*.bin) Binary task list files", "bin");
                final JFileChooser fileChooser = new JFileChooser(defaultPath);
                fileChooser.setFileFilter(filter);
                int returnVal = fileChooser.showOpenDialog(mainController.getMainForm());

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    mainController.setModel(mainController.loadFromFile(file));
                    mainController.getModel().setSaved(true);
                    mainController.updateView();
                    logger.info("Task list is being loaded from file: " + fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
    }

    protected void setSaveButtonListener() {
        mainController.getMainForm().getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonHandler();
            }
        });
    }

    protected void saveButtonHandler() {
        logger.info("Task list is being saved to file");
        TaskIO.writeBinary(mainController.getModel(), new File(mainController.getFileName()));
        mainController.getModel().setSaved(true);
    }

    //Метод обновляет выпадающий список согласно текущей модели
    protected void updateCombobox(JComboBox combobox) {
        combobox.removeAllItems();
        if (mainController.getModel().size() == 0) {
            combobox.addItem("No tasks founded");
        }
        else {
            for (Task t : mainController.getModel()) {
                combobox.addItem(t.toString());
            }
        }
    }
}
