package ua.edu.sumdu.j2se.dmitry.model;

import java.util.Date;
import java.util.Iterator;
import java.util.Observable;

/**
 * Created by DarkST on 24.11.2016.
 */
public abstract class TaskList extends Observable implements Iterable<Task>, Cloneable{
    public abstract void add (Task task);
    public abstract boolean remove (Task task);
    public abstract int size ();
    public abstract Task getTask (int index);
    public abstract Iterator<Task> iterator();
    private boolean saved = true;

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public Observable observable() {
        return this;
    }

    @Override
    public TaskList clone () throws CloneNotSupportedException{
        return (TaskList) super.clone ();
    }

    @Override
    public String toString() {
        String result = new String();
        for (Task temp : this) {
            result += temp.toString() + "\n";
        }
        return result;
    }


    public TaskList incoming (Date from, Date to) throws IllegalAccessException, InstantiationException {
        TaskList tempArrayTaskList = this.getClass().newInstance();
        for (int i = 0; i < this.size(); i ++) {
            Task tempTask = this.getTask(i);
            if (tempTask != null) {
                if (tempTask.isActive() == true) {
                    if ((tempTask.isRepeated() && tempTask.nextTimeAfter(from).compareTo(to) < 0 && tempTask.nextTimeAfter(from) != null) || (!tempTask.isRepeated() && tempTask.getTime().compareTo(from) > 0 && tempTask.getTime().compareTo(from) <= 0)) {
                        tempArrayTaskList.add(tempTask);
                    }
                }
            }
        }
        return tempArrayTaskList;
    }

}
