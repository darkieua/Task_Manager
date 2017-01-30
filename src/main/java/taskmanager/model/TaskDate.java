package taskmanager.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DarkST on 11.12.2016.
 */

public class TaskDate implements Cloneable{
    private String title;
    private Date time;
    private Date start;
    private Date end;
    private int interval;

    private boolean active;
    private boolean repeated;

    public TaskDate (String title, Date time) {
        this.title = title;
        this.time = time;
        this.active = false;
        this.repeated = false;
    }

    public TaskDate (String title, Date start, Date end, int interval) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.interval = interval;
        this.active = false;
        this.repeated = true;
    }

    public String getTitle () {
        return this.title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public boolean isActive () {
        return this.active;
    }

    public void setActive (boolean active) {
        this.active = active;
    }

    //Методи для зчитування та зміни часу виконання для задач, що не повторюються
    public Date getTime () {
        if (!repeated) {
            return this.time;
        }
        else return start;
    }

    public void setTime (Date time) {
        this.repeated = false;
        this.time = time;
    }

    //Методи для зчитування та зміни часу виконання для задач, що повторюються
    public Date getStartTime () {
        return repeated ? this.start : this.time;
    }

    public Date getEndTime () {
        return repeated ? this.end : this.time;
    }

    public int getRepeatInterval () {
        return repeated ? this.interval : null;
    }

    public void setTime (Date start, Date end, int interval) {
        if (!this.repeated) {
            this.repeated = true;
            this.start = start;
            this.end = end;
            this.interval = interval;
        }
    }

    public Date nextTimeAfter (Date current) {
        Date next;
        if (active == true) {
            if (this.repeated) {
                next = start;
                while (!next.after(current)) {
                    next = new Date(next.getTime() + interval);
                }
                return !next.after(end) ? next : null;
            } else if (!current.after(this.time)) {
                return this.time;
            }
        }
        return null;
    }

    public boolean isRepeated () {
        return this.repeated;
    }
}
