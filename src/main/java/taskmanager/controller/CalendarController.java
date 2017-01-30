package taskmanager.controller;

import taskmanager.model.Task;
import taskmanager.model.Tasks;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.*;

/**
 * Created by darkie on 22.01.17.
 */
public class CalendarController extends Controller {

    private Controller controller;

    public CalendarController (Controller controller) {
        this.controller = controller;
    }

    protected void createCalendar() {
        controller.getMainForm().getFromField().setText(Controller.dateFormat.format(new Date(0)));
        controller.getMainForm().getTillField().setText(Controller.dateFormat.format(new Date(new Date().getTime() * 2)));
        controller.getMainForm().getApplyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    controller.dateFormat.parse(controller.getMainForm().getFromField().getText());
                    controller.dateFormat.parse(controller.getMainForm().getFromField().getText());
                } catch (ParseException e) {
                    controller.throwError("Wrong date input");
                } finally {
                    updateCalendar();
                }
            }
        });
        updateCalendar();
    }

    protected void updateCalendar() {

        String calendarStr = new String();
        try {
            if (controller.getModel() == null) {
                System.out.println("controller = null");
            }

            SortedMap<Date, Set<Task>> map = Tasks.calendar(
                    controller.getModel(),
                    controller.dateFormat.parse(controller.getMainForm().getFromField().getText()),
                    controller.dateFormat.parse(controller.getMainForm().getTillField().getText())
            );

            for (Date key : map.keySet()) {
                calendarStr += Controller.dateFormat.format(key) + ": ";
                for (Task t : map.get(key)) {
                   calendarStr += "\"" + t.getTitle() + "\" ";
                }
                calendarStr += "\n";
            }

            controller.getMainForm().getCalendarArea().setText(calendarStr);
        } catch (ParseException e) {
            controller.throwError("Wrong input");
        }
    }
}
