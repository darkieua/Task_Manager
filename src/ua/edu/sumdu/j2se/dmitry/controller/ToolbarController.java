package ua.edu.sumdu.j2se.dmitry.controller;

import ua.edu.sumdu.j2se.dmitry.model.Task;
import ua.edu.sumdu.j2se.dmitry.model.TaskIO;

import javax.swing.*;
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
    //Метод вешает слушатель на кнопку редактирования
    public static void setEditButtonListener() {
        mainForm.getButtonEdit().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (model.size() > 0) {
                    EditController.throwEditDialog(model.getTask(mainForm.getTaskCombobox().getSelectedIndex()), mainForm.getTaskCombobox().getSelectedIndex());
                }
                else {
                    throwError("Task list is empty");
                }
            }
        });
    }

    //Метод вешает слушатель на кнопку удаления задачи
    public static void setRemoveButtonListener() {
        mainForm.getRemoveButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (model.size() > 0) {
                    model.remove(model.getTask(mainForm.getTaskCombobox().getSelectedIndex()));
                    updateCombobox(mainForm.getTaskCombobox());
                    updateView();
                }
                else {
                    throwError("Cannot remove, task list is empty already");
                }
            }
        });
    }

    public static void setAddButtonListener() {
        mainForm.getAddButon().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Add button pressed");

                Task newTask = new Task("Title", new Date());
                EditController.throwEditDialog(newTask, 0);
                model.add(newTask);
                updateCombobox(mainForm.getTaskCombobox());
                updateView();

            }
        });
    }

    public static void setLoadButtonListener() {
        mainForm.getLoadButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TaskIO.readBinary(model, new File(FILE_NAME));
                    updateView();
                } catch (FileNotFoundException ex) {
                    Controller.throwError("Autosave file not found. First run?");
                }
            }
        });
    }

    public static void setSaveButtonListener() {
        mainForm.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TaskIO.writeBinary(model, new File(FILE_NAME));
            }
        });
    }

    //Метод обновляет выпадающий список согласно текущей модели
    protected static void updateCombobox(JComboBox combobox) {
        combobox.removeAllItems();
        if (model.size() == 0) {
            combobox.addItem("No tasks founded");
        }
        else {
            for (Task t : model) {
                combobox.addItem(t.toString());
            }
        }
    }
}
