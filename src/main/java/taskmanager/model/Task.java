package taskmanager.model;

import taskmanager.controller.MainController;

import java.util.Date;

/**
 * Created by DarkST on 11.12.2016.
 */

public class Task implements Cloneable{
    private String title;
    private Date time;
    private Date start;
    private Date end;
    private int interval;

    private boolean active;
    private boolean repeated;

    public Task (String title, Date time) {
        this.title = title;
        this.time = time;
        this.active = false;
        this.repeated = false;
    }

    public Task (String title, Date start, Date end, int interval) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.interval = interval * 1000;
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

    public long getRepeatInterval () {
        return repeated ? this.interval : -1;
    }

    public void setTime (Date start, Date end, int interval) {
        if (!this.repeated) {
            this.repeated = true;
            this.start = start;
            this.end = end;
            this.interval = interval;
        }
    }

    public Date nextTimeAfter (Date current)  {
        if (isActive()) {
            if (isRepeated() && current.before(end)) {
                Date temp = getStartTime();
                while (!temp.after(current))
                    temp = new Date(temp.getTime() + getRepeatInterval());
                if (!temp.after(end)) return temp;
                else return null;
            } else if (current.before(getTime())) {
                return getTime();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public boolean isRepeated () {
        return this.repeated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (active != task.active) return false;
        if (repeated != task.repeated) return false;
        if (repeated) {
            if (interval != task.interval) return false;
            if (!start.equals(task.start)) return false;
            if (!end.equals(task.end)) return false;
        }
        else if (!time.equals(task.time)) return false;
        return title.equals(task.title);

    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        if (repeated) {
            result = 31 * result + start.hashCode();
            result = 31 * result + end.hashCode();
            result = 31 * result + interval;
        }
        else result = 31 * result + time.hashCode();
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (repeated ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        String result;
        if (isRepeated()) {
            result = "\"" + getTitle() + "\" from [" + MainController.dateFormat.format(getStartTime()) + "] to [" + MainController.dateFormat.format(getEndTime()) + "] every " + TaskIO.intervalFormat(getRepeatInterval()) + (isActive() ? " active" : "");
        }
        else
            result = "\"" + getTitle() + "\" at [" + MainController.dateFormat.format(getTime()) + (isActive() ? "] active" : "]");
        return result;
    }
}