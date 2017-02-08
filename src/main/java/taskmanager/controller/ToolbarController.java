package taskmanager.controller;

import org.apache.log4j.Logger;
import taskmanager.Main;
import taskmanager.model.Task;
import taskmanager.model.TaskIO;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

/**
 * Created by darkie on 22.01.17.
 * Класс, отвечающий за поведение элементов панели инструментов (выпадающий список задач и кнопки),
 * которая находится на форме вне зависимости от выбраной вкладки
 */
public class ToolbarController extends Controller {



    private Controller controller;
    private EditController edit;

    public ToolbarController (Controller controller) {
        this.controller = controller;
        this.edit = new EditController(controller);
    }

    //Метод вешает слушатель на кнопку редактирования
    protected void setEditButtonListener() {
        controller.getMainForm().getButtonEdit().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (controller.getModel().size() > 0) {
                    edit.throwEditDialog(controller.getModel().getTask(controller.getMainForm().getTaskCombobox().getSelectedIndex()), controller.getMainForm().getTaskCombobox().getSelectedIndex());
                    controller.getModel().setSaved(false);
                    logger.info("Task \"" + controller.getModel().getTask(controller.getMainForm().getTaskCombobox().getSelectedIndex()).toString() + "\" is being edited");
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
        controller.getMainForm().getRemoveButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (controller.getModel().size() > 0) {
                    logger.info("Task \"" + controller.getModel().getTask(controller.getMainForm().getTaskCombobox().getSelectedIndex()).toString() + "\" was removed");
                    controller.getModel().remove(controller.getModel().getTask(controller.getMainForm().getTaskCombobox().getSelectedIndex()));
                    controller.getModel().setSaved(false);
                    updateCombobox(controller.getMainForm().getTaskCombobox());
                    controller.updateView();
                }
                else {
                    logger.error("Tried to remove task from empty task list");
                    throwError("Cannot remove, task list is empty already");
                }
            }
        });
    }

    protected void setAddButtonListener() {
        controller.getMainForm().getAddButon().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Task newTask = new Task("Title", new Date());
                newTask.setActive(true);
                edit.throwEditDialog(newTask, 0);
                controller.getModel().add(newTask);
                controller.getModel().setSaved(false);
                updateCombobox(controller.getMainForm().getTaskCombobox());
                controller.updateView();
            }
        });
    }

    protected void setLoadButtonListener() {
        controller.getMainForm().getLoadButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                controller.setModel(controller.loadFromFile(controller.getFileName()));
                //TaskIO.readBinary(controller.getModel(), new File(controller.getFileName()));
                controller.getModel().setSaved(true);
                controller.updateView();
                logger.info("Task list is being loaded from file");
                */
                String defaultPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath(); //Path of Main class, used as a default in filechooser
                FileNameExtensionFilter filter = new FileNameExtensionFilter("(*.bin) Binary task list files", "bin");
                final JFileChooser fileChooser = new JFileChooser(defaultPath);
                fileChooser.setFileFilter(filter);
                int returnVal = fileChooser.showOpenDialog(controller.getMainForm());

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    controller.setModel(controller.loadFromFile(file));
                    controller.getModel().setSaved(true);
                    controller.updateView();
                    logger.info("Task list is being loaded from file: " + fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
    }

    protected void setSaveButtonListener() {
        controller.getMainForm().getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonHandler();
            }
        });
    }

    protected void saveButtonHandler() {
        logger.info("Task list is being saved to file");
        TaskIO.writeBinary(controller.getModel(), new File(controller.getFileName()));
        controller.getModel().setSaved(true);
    }

    //Метод обновляет выпадающий список согласно текущей модели
    protected void updateCombobox(JComboBox combobox) {
        combobox.removeAllItems();
        if (controller.getModel().size() == 0) {
            combobox.addItem("No tasks founded");
        }
        else {
            for (Task t : controller.getModel()) {
                combobox.addItem(t.toString());
            }
        }
    }
}
