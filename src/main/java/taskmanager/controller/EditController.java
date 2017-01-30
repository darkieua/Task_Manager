package taskmanager.controller;

import taskmanager.model.Task;
import taskmanager.view.EditDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;


/**
 * Created by darkie on 22.01.17.
 * Класс, отвечающий за поведение элементов в диалоговом окне изменения задания
 */
public class EditController extends Controller {

    private static Task task;
    private Controller controller;

    public EditController (Controller controller) {
        this.controller = controller;
    }

    //Метод открывает новое диалоговое окно редактирования задачи (сама задача как аргумент метода)
    protected void throwEditDialog (Task taskarg, int index) {
        if (taskarg == null) {
            taskarg = new Task ("Task title", new Date());
        }
        task = taskarg;
        EditDialog edit = new EditDialog(this.controller);
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
                edit.getStartField().setText(Controller.dateFormat.format(task.getStartTime()));
                edit.getEndField().setText(Controller.dateFormat.format(task.getEndTime()));
                edit.getIntervalField().setText(Objects.toString(task.getRepeatInterval()));
            } else {
                edit.setFormRepeated(false);
                edit.getDateField().setText(Controller.dateFormat.format(task.getTime()));
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
                            try {
                                task.setTime(
                                        Controller.dateFormat.parse(edit.getStartField().getText()),
                                        Controller.dateFormat.parse(edit.getEndField().getText()),
                                        new Integer(edit.getIntervalField().getText())
                                );
                            } catch (ParseException e) {
                                //throwError(e.toString());
                                controller.throwError("Date should be entered in format: dd-MM-yyyy HH:mm:ss.SSS");
                            }
                        } else {
                            try {
                                task.setTime(Controller.dateFormat.parse(edit.getDateField().getText()));
                            } catch (ParseException e) {
                                controller.throwError("Date should be entered in format: dd-MM-yyyy HH:mm:ss.SSS");
                            }
                        }
                    }

                    //После изменения задачи, обновляется вид
                    controller.updateView();
                }
            });
    }
}
