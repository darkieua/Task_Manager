package taskmanager.controller;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.TimePicker;
import taskmanager.model.Task;
import taskmanager.model.Tasks;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by darkie on 22.01.17.
 */
public class CalendarController extends MainController {

    private final int MAX_TASKS_SHOWN = 500; //Maximum amount of tasks, which can be shown in calendar;
    private MainController mainController;
    private long fromTimeMillis;
    private long tillTimeMillis;

    public CalendarController (MainController mainController) {
        this.mainController = mainController;
    }

    protected void createCalendar() {
        //From and Till DateTimePickers
        DateTimePicker fromDateTimePicker = mainController.getMainForm().getFromDatePicker();
        DateTimePicker tillDateTimePicker = mainController.getMainForm().getTillDatePicker();

        //From DatePicker and TimePicker
        DatePicker fromDatePicker = fromDateTimePicker.getDatePicker();
        TimePicker fromTimePicker = fromDateTimePicker.getTimePicker();

        //Till DatePicker and TimePicker
        DatePicker tillDatePicker = tillDateTimePicker.getDatePicker();
        TimePicker tillTimePicker = tillDateTimePicker.getTimePicker();

        mainController.getMainForm().getApplyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (fromDatePicker.getDate() != null && fromTimePicker.getTime() != null && tillDatePicker.getDate() != null && tillTimePicker.getTime() != null) {
                    long offset = TimeZone.getDefault().getOffset(new Date().getTime()); //Local timezone offset
                    fromTimeMillis = (fromDatePicker.getDate().toEpochDay() * 86400 * 1000) + (fromTimePicker.getTime().toSecondOfDay() * 1000) - offset;
                    tillTimeMillis = (tillDatePicker.getDate().toEpochDay() * 86400 * 1000) + (tillTimePicker.getTime().toSecondOfDay() * 1000) - offset;

                    if (fromTimeMillis > tillTimeMillis) {
                        mainController.logger.error("\"Till\" date can not be more than \"From\"");
                        mainController.throwError("\"Till\" date can not be more than \"From\"");
                    }
                    else {
                        DateFormat gmtFormat = new SimpleDateFormat();
                        System.out.println();
                        System.out.println("From: " + fromTimeMillis + ", " + gmtFormat.format(new Date(fromTimeMillis)));
                        System.out.println("Till: " + tillTimeMillis + ", " + gmtFormat.format(new Date(tillTimeMillis)));
                        updateCalendar();
                    }
                }
                else {
                    mainController.logger.error("Date is not set correctly");
                    mainController.throwError("Date is not set correctly");
                }
            }
        });
    }

    protected void updateCalendar() {
        String calendarStr = new String();

        SortedMap<Date, Set<Task>> map = Tasks.calendar(mainController.getModel(), new Date(fromTimeMillis), new Date(tillTimeMillis));
        int count = 0;
        outerloop: for (Date key : map.keySet()) {
            calendarStr += MainController.dateFormat.format(key) + ": ";
            count++;
            for (Task t : map.get(key)) {
                calendarStr += "\"" + t.getTitle() + "\" ";
                if (count > MAX_TASKS_SHOWN) {
                    mainController.throwError("Too much tasks on this period! First " + MAX_TASKS_SHOWN + " are shown.");
                    break outerloop;
                }
            }
            calendarStr += "\n";
        }

       mainController.getMainForm().getCalendarArea().setText(calendarStr);
    }
}
