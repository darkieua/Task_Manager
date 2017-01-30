package ua.edu.sumdu.j2se.dmitry.model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by DarkST on 19.10.2016.
 */
public class ArrayTaskList extends TaskList {

    @Override
    public Iterator<Task> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Task> {
        private int end = size;
        private int current = 0;

        @Override
        public boolean hasNext() {
            return current < end;
        }

        @Override
        public Task next() {
            if (!hasNext()) throw new NoSuchElementException();
            return getTask(current++);
        }

        @Override
        public void remove () {
            if (current == 0){
                throw new IllegalStateException("remove() method should be used after next()");
            }
            ArrayTaskList.this.remove(getTask(current-1));
            current--;
        }
    }

    private Task[] taskList = new Task[5];
    private int size = 0;

    @Override
    public void add (Task task) {
        if (task == null) throw new IllegalArgumentException("Attempt to add a null task");
        if (size < taskList.length) {
            taskList[size] = task;
        } else {
            taskList = Arrays.copyOf(taskList, size + (int)(size * 0.25) + 1);
            taskList[size] = task;
        }
        size++;
    }

    @Override
    public boolean remove (Task task) {
        if (task == null) throw new IllegalArgumentException("Attempt to remove a null task");
        int index = indexOfElement(task);
        if (index >= 0) {
            Task newTaskList[] = new Task[taskList.length - 1];

            System.arraycopy(taskList, 0, newTaskList, 0, index);

            if (index < taskList.length - 1) {
                System.arraycopy(taskList, index + 1, newTaskList, index, taskList.length - index - 1);
            }

            taskList = newTaskList;
            size--;
            return true;
        }
        else return false;
    }

    @Override
    public int size () {
        return size;
    }

    @Override
    public Task getTask (int index) {
        if (index < 0) throw new IllegalArgumentException("Index of element is less then zero");
        if (index > size) throw new ArrayIndexOutOfBoundsException("Index of element greater than size");
        return taskList[index];
    }

    public int indexOfElement (Task task) {
        if (task == null) throw new IllegalArgumentException("Attempt to get an index of a null task");
        for (int i = 0; i < taskList.length; i ++) {
            if (taskList[i] == task) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskList list = (TaskList)o;
        if (this.size() != list.size()) return false;
        for (int i = 0; i < this.size(); i ++) {
            if (!(this.getTask(i).equals(list.getTask(i)))) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(taskList);
    }

    @Override
    public ArrayTaskList clone () throws CloneNotSupportedException {
        ArrayTaskList list = new ArrayTaskList();
        for (Task temp : this) {
            list.add(temp);
        }
        return list;
    }

}
