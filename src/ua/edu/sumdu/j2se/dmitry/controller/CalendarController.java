package ua.edu.sumdu.j2se.dmitry.controller;

import ua.edu.sumdu.j2se.dmitry.model.Task;
import ua.edu.sumdu.j2se.dmitry.model.Tasks;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.*;

import static ua.edu.sumdu.j2se.dmitry.model.Tasks.calendar;

/**
 * Created by darkie on 22.01.17.
 */
public class CalendarController {

    protected static void createCalendar() {
        Controller.getMainForm().getFromField().setText(Controller.dateFormat.format(new Date(0)));
        Controller.getMainForm().getTillField().setText(Controller.dateFormat.format(new Date(new Date().getTime() * 2)));
        Controller.getMainForm().getApplyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    Controller.dateFormat.parse(Controller.getMainForm().getFromField().getText());
                    Controller.dateFormat.parse(Controller.getMainForm().getFromField().getText());
                } catch (ParseException e) {
                    Controller.throwError("Wrong date input");
                } finally {
                    updateCalendar();
                }
            }
        });
        updateCalendar();
    }

    protected static void updateCalendar() {

        String calendarStr = new String();
        try {
            SortedMap<Date, Set<Task>> map = Tasks.calendar(
                    Controller.getModel(),
                    Controller.dateFormat.parse(Controller.getMainForm().getFromField().getText()),
                    Controller.dateFormat.parse(Controller.getMainForm().getTillField().getText())
            );

            for (Date key : map.keySet()) {
                calendarStr += Controller.dateFormat.format(key) + ": ";
                for (Task t : map.get(key)) {
                   calendarStr += "\"" + t.getTitle() + "\" ";
                }
                calendarStr += "\n";
            }

            Controller.getMainForm().getCalendarArea().setText(calendarStr);
        } catch (ParseException e) {
            Controller.throwError("Wrong input");
        }

    }
}
