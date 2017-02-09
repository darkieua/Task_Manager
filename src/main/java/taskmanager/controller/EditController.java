package taskmanager.controller;

import taskmanager.model.Task;
import taskmanager.view.EditDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;


/**
 * Created by darkie on 22.01.17.
 * Класс, отвечающий за поведение элементов в диалоговом окне изменения задания
 */
public class EditController extends MainController {

    private static Task task;
    private MainController mainController;

    public EditController (MainController mainController) {
        this.mainController = mainController;
    }

    //Метод открывает новое диалоговое окно редактирования задачи (сама задача как аргумент метода)
    protected void throwEditDialog (Task taskarg, int index) {
        if (taskarg == null) {
            taskarg = new Task ("Task title", new Date());
        }
        task = taskarg;
        EditDialog edit = new EditDialog(this.mainController);
        edit.setEditedTaskIndex(index);
        setEditDialog(taskarg, edit);
        edit.pack();
        edit.setVisible(true);
    }

    //Метод задает соответствие между элементами формы редактирования и данными из объекта задачи
    protected void setEditDialog(Task task, EditDialog edit) {
        if (task != null) {
            edit.setEditedTask(task);
            edit.getTitleField().setText(task.getTitle());
            if (task.isRepeated()) {
                edit.setFormRepeated(true);

                edit.getStartDatepicker().setDateTimePermissive(task.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                edit.getEndDatepicker().setDateTimePermissive(task.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                edit.getIntervalField().setText(Objects.toString(task.getRepeatInterval()));
            } else {
                edit.setFormRepeated(false);
                edit.getDateDatepicker().setDateTimePermissive(task.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            }
            edit.getActiveCheckBox().setSelected(task.isActive());
            edit.getRepeatedCheckBox().setSelected(task.isRepeated());

            //Вешается листенер на чекбокс повторения задачи
            //Если checked (т.е. задача повторяется), то блокируются поля формы, которые отвечают за редактирования неповторяющейся задачи
            //Если unchecked (т.е. задача не повторяется), то блокируются поля, отвечающие за редактирование повторяющейся задачи
            edit.getRepeatedCheckBox().addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent itemEvent) {
                    if (edit.getRepeatedCheckBox().isSelected()) {
                        edit.getEditedTask().setActive(true);
                        edit.setFormRepeated(true);
                    } else {
                        edit.getEditedTask().setActive(false);
                        edit.setFormRepeated(false);
                    }
                }
            });

            //Вешается листенер на чекбокс активности задачи
            edit.getActiveCheckBox().addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent itemEvent) {
                    if (edit.getActiveCheckBox().isSelected()) {
                        edit.getEditedTask().setActive(true);
                    } else {
                        edit.getEditedTask().setActive(false);
                    }
                }
            });
        }

        //Вешается чекбокс на кнопку сохранения
        //При нажатии, в текущую модель отправляется отредактированная задача
        edit.getButtonSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (task != null) {
                    task.setTitle(edit.getTitleField().getText());
                    task.setActive(edit.getActiveCheckBox().isSelected());
                    if (edit.getRepeatedCheckBox().isSelected()) {
                        if (edit.getStartDatepicker().getDateTimePermissive() != null && edit.getEndDatepicker().getDateTimePermissive() != null && !edit.getIntervalField().getText().isEmpty()) {
                            task.setTime(
                                    new Date().from(edit.getStartDatepicker().getDateTimePermissive().atZone(ZoneId.systemDefault()).toInstant()),
                                    new Date().from(edit.getEndDatepicker().getDateTimePermissive().atZone(ZoneId.systemDefault()).toInstant()),
                                    new Integer(edit.getIntervalField().getText())
                            );
                            mainController.addNewTask(task);
                            logger.info("Task \"" + task.toString() + "\" saved");
                            edit.dispose();
                        } else {
                            String errorMsg = "Tried to set an empty start or end date to repeated task";
                            mainController.throwError(errorMsg);
                            System.out.println(errorMsg);
                            logger.error(errorMsg);
                        }
                    } else {
                        if (edit.getDateDatepicker() != null) {
                            task.setTime(new Date().from(edit.getDateDatepicker().getDateTimePermissive().atZone(ZoneId.systemDefault()).toInstant()));
                            mainController.addNewTask(task);
                            logger.info("Task \"" + task.toString() + "\" saved");
                            edit.dispose();
                        }
                        else {
                            String errorMsg = "Tried to set an empty execution date to  non-repeated task";
                            mainController.throwError(errorMsg);
                            System.out.println(errorMsg);
                            logger.error(errorMsg);
                        }
                    }
                }
            }
        });

        edit.getButtonCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                edit.dispose();
            }
        });
    }
}
